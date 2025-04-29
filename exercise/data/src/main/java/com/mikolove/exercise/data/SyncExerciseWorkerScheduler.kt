package com.mikolove.exercise.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.mikolove.core.data.workers.SyncExerciseScheduler
import com.mikolove.core.database.database.ExercisePendingSyncDao
import com.mikolove.core.database.mappers.toExerciseCacheEntity
import com.mikolove.core.database.model.DeletedExerciseSyncEntity
import com.mikolove.core.database.model.ExercisePendingSyncEntity
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncExerciseWorkerScheduler (
    private val context : Context,
    private val pendingSyncDao: ExercisePendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope : CoroutineScope
    ): SyncExerciseScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncExerciseScheduler.SyncType) {
       when(type){
           is SyncExerciseScheduler.SyncType.CreateExercise -> scheduleCreateExerciseWorker(type.exercise)
           is SyncExerciseScheduler.SyncType.DeleteExercise -> scheduleDeleteExerciseWorker(type.exerciseId)
           is SyncExerciseScheduler.SyncType.FetchExercise -> scheduleFetchExerciseWorker(type.interval)
       }
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    private suspend fun scheduleFetchExerciseWorker(interval : Duration){

        val isSyncScheduled = withContext(Dispatchers.IO){
            val result = workManager.getWorkInfosByTag("fe_sync_work").get()
            result.isNotEmpty()
        }

        if(isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchExerciseWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("fe_sync_work")
            .build()

        workManager.enqueue(workRequest).await()

    }

    private suspend fun scheduleCreateExerciseWorker(exercise : Exercise){
        Timber.d("Create exercise worker start")
        val userId = sessionStorage.get()?.userId ?: return

        val pendingExercise = ExercisePendingSyncEntity(
            idExercise = exercise.idExercise,
            exercise = exercise.toExerciseCacheEntity(userId),
            idUser = userId
        )

        pendingSyncDao.upsertExercisePendingSyncEntity(pendingExercise)

        Timber.d("pendingSync executed for $pendingExercise")

        val workRequest = OneTimeWorkRequestBuilder<CreateExerciseWorker>()
            .addTag("ce_sync_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(CreateExerciseWorker.EXERCISE_ID, exercise.idExercise)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteExerciseWorker(exerciseId : String){
        val userId = sessionStorage.get()?.userId ?: return

        val pendingDelete = DeletedExerciseSyncEntity(
            idExercise = exerciseId,
            idUser = userId
        )

        pendingSyncDao.upsertDeletedExerciseSyncEntity(pendingDelete)

        val workRequest = OneTimeWorkRequestBuilder<DeleteExerciseWorker>()
            .addTag("de_sync_work")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteExerciseWorker.EXERCISE_ID, exerciseId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

}