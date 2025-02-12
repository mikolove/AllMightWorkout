package com.mikolove.exercise.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.workers.toWorkerResult
import com.mikolove.core.database.database.ExercisePendingSyncDao
import com.mikolove.core.database.mappers.toExercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.util.Result.Error
import com.mikolove.core.domain.util.Result.Success

class  CreateExerciseWorker(
    context : Context,
    private val params : WorkerParameters,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val pendingSyncDao: ExercisePendingSyncDao
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5){
            return Result.failure()
        }

        val pendingExerciseId = params.inputData.getString(EXERCISE_ID) ?: return Result.failure()
        val pendingExerciseEntity = pendingSyncDao.getExercisePendingSyncEntity(pendingExerciseId) ?: return Result.failure()

        val exercise = pendingExerciseEntity.exercise.toExercise()

        return when(val result = safeApiCall { exerciseNetworkDataSource.upsertExercise(exercise) }){
            is Error ->{
                result.error.toWorkerResult()
            }
            is Success -> {
                pendingSyncDao.deleteExercisePendingSyncEntity(pendingExerciseId)
                Result.success()
            }
        }

    }

    companion object {
        const val EXERCISE_ID = "EXERCISE_ID"
    }
}