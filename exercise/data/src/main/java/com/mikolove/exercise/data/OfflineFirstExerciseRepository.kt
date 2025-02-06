package com.mikolove.exercise.data

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OfflineFirstExerciseRepository(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val applicationScope : CoroutineScope,
) : ExerciseRepository{


    override suspend fun getExercise(exerciseId: String): Result<Exercise, DataError.Local> {
        return safeCacheCall {
            exerciseCacheDataSource.getExerciseById(exerciseId)
        }.map { it }
    }

    override fun getExercises(userId : String): Flow<List<Exercise>> {
        return exerciseCacheDataSource.getExercises(userId)
    }

    override suspend fun fetchExercises(userId : String): EmptyResult<DataError> {
        return when(val result = safeApiCall{exerciseNetworkDataSource.getExercises()}){
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success->{
                applicationScope.async {
                    safeCacheCall {
                        exerciseCacheDataSource.upsertExercises(result.data,userId)
                    }.asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertExercise(exercise: Exercise,userId : String): EmptyResult<DataError> {
        val cacheResult = safeCacheCall {
            exerciseCacheDataSource.upsertExercise(exercise,userId)
        }
        if(cacheResult !is Result.Success){
            return cacheResult.asEmptyDataResult()
        }

        val networkResult = safeApiCall {
            exerciseNetworkDataSource.upsertExercise(exercise)
        }

        return when(networkResult){
            is Result.Error -> {
                applicationScope.launch {
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
    }

    override suspend fun syncPendingExercises(userId: String) {
        TODO("Not yet implemented")
    }
}