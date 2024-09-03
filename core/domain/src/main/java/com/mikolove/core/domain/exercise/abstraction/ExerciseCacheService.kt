package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseCacheService {

    suspend fun upsertExercise(exercise: Exercise, idUser : String) : Long

    suspend fun removeExercises(exercises: List<Exercise>)  : Int

    suspend fun getExercises(idUser : String) : Flow<List<Exercise>>

    suspend fun getExerciseById(primaryKey: String) : Exercise
}