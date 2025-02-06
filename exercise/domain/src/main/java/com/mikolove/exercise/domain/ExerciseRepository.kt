package com.mikolove.exercise.domain

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getExercises(userId :String): Flow<List<Exercise>>

    suspend fun getExercise(exerciseId : String) : Result<Exercise, DataError>

    suspend fun fetchExercises(userId: String): EmptyResult<DataError>

    suspend fun upsertExercise( exercise: Exercise,userId : String) : EmptyResult<DataError>

    suspend fun deleteExercise(exerciseId : String)

    suspend fun syncPendingExercises(userId: String)
}