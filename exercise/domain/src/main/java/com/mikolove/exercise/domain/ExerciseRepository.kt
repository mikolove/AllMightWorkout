package com.mikolove.exercise.domain

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getExercises(): Flow<List<Exercise>>

    suspend fun fetchExercises(): EmptyResult<DataError>

    suspend fun upsertExercise( exercise: Exercise) : EmptyResult<DataError>

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun syncPendingExercises()
}