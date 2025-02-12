package com.mikolove.exercise.data

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.data.workers.SyncExerciseScheduler
import com.mikolove.core.database.database.ExercisePendingSyncDao
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.util.map
import com.mikolove.exercise.domain.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class OfflineFirstExerciseRepository(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val exercisePendingSyncDao: ExercisePendingSyncDao,
    private val sessionStorage : SessionStorage,
    private val applicationScope : CoroutineScope,
    private val syncExerciseScheduler: SyncExerciseScheduler,
) : ExerciseRepository {


    override suspend fun getExercise(exerciseId: String): Result<Exercise, DataError.Local> {
        return safeCacheCall {
            exerciseCacheDataSource.getExerciseById(exerciseId)
        }.map { it }
    }

    override fun getExercises(): Flow<List<Exercise>> = flow {
        val userId = sessionStorage.get()?.userId ?: return@flow
        emitAll(exerciseCacheDataSource.getExercises(userId))
    }

    override fun getExercisesByWorkoutTypes(workoutTypes: List<String>): Flow<List<Exercise>> = flow {
        val userId = sessionStorage.get()?.userId ?: return@flow
        emitAll(exerciseCacheDataSource.getExercisesByWorkoutTypes(workoutTypes,userId))
    }

    override suspend fun fetchExercises(): EmptyResult<DataError> {
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        return when (val result = safeApiCall { exerciseNetworkDataSource.getExercises() }) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    safeCacheCall {
                        exerciseCacheDataSource.upsertExercises(result.data, userId)
                    }.asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertExercise(
        exercise: Exercise,
    ): EmptyResult<DataError> {

        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        val exerciseCacheResult = safeCacheCall {
            exerciseCacheDataSource.upsertExercise(exercise, userId)
        }
        if (exerciseCacheResult !is Result.Success) {
            return exerciseCacheResult.asEmptyDataResult()
        }

        val bodyPartsCacheResult = safeCacheCall {
            exerciseCacheDataSource.insertBodyPartsAndClean(
                exercise.idExercise,
                exercise.bodyParts
            )
        }
        if (bodyPartsCacheResult !is Result.Success) {
            return bodyPartsCacheResult.asEmptyDataResult()
        }

        val networkResult = safeApiCall {
            exerciseNetworkDataSource.upsertExercise(exercise)
        }

        return when (networkResult) {
            is Result.Error -> {
                applicationScope.launch {
                    syncExerciseScheduler.scheduleSync(
                        type =SyncExerciseScheduler.SyncType.CreateExercise(exercise =exercise)
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override suspend fun deleteExercise(exerciseId: String) {
        safeCacheCall {
            exerciseCacheDataSource.removeExercise(exerciseId)
        }

        //Exercise created in offline mode and delete as well.
        val isPendingSync = exercisePendingSyncDao.getExercisePendingSyncEntity(exerciseId) != null
        if(isPendingSync){
            exercisePendingSyncDao.deleteExercisePendingSyncEntity(idExercise = exerciseId)
            return
        }

        val remoteResult = applicationScope.async{
            safeApiCall {
                exerciseNetworkDataSource.removeExercise(exerciseId)
            }
        }.await()

        if(remoteResult is Result.Error){
            applicationScope.launch {
                syncExerciseScheduler.scheduleSync(
                    type = SyncExerciseScheduler.SyncType.DeleteExercise(exerciseId = exerciseId)
                )
            }.join()
        }

    }

    override suspend fun syncPendingExercises() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdExercises = async {
                exercisePendingSyncDao.getAllExercisePendingSyncEntities(userId)
            }

            val deletedExercises = async {
                exercisePendingSyncDao.getAllDeletedExerciseSyncEntities(userId)
            }

            val createdJobs = createdExercises.await()
                .map {
                    launch {
                        val cacheResult = safeCacheCall {
                            exerciseCacheDataSource.getExerciseById(it.idExercise)
                        }
                        if(cacheResult !is Result.Success){
                            return@launch
                        }
                        val exercise = cacheResult.data

                        when (safeApiCall {
                            exerciseNetworkDataSource.upsertExercise(exercise)
                        }) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    exercisePendingSyncDao.deleteExercisePendingSyncEntity(it.idExercise)
                                }.join()
                            }
                        }
                    }
                }

            val deletedJobs = deletedExercises.await()
                .map {
                    launch {
                        when (safeCacheCall {
                            exerciseCacheDataSource.removeExercise(it.idExercise)
                        }) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    exercisePendingSyncDao.deleteDeletedExerciseSyncEntity(it.idExercise)
                                }.join()
                            }
                        }
                    }
                }

            createdJobs.forEach { it.join() }
            deletedJobs.forEach { it.join() }
        }
    }
}