package com.mikolove.exercise.data

import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.exercise.domain.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class OfflineFirstExerciseRepository(
    val exerciseCacheDataSource: ExerciseCacheDataSource,
    val exerciseNetworkDataSource: ExerciseNetworkDataSource,
) : ExerciseRepository{

    override fun getExercises(userId : String): Flow<List<Exercise>> {
        return exerciseCacheDataSource.getExercises(userId)
    }

    override suspend fun fetchExercises(): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertExercise(exercise: Exercise): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun syncPendingExercises() {
        TODO("Not yet implemented")
    }
}