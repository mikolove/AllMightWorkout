package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.exercise.Exercise

interface ExerciseNetworkDataSource {

    suspend fun getExercises() : List<Exercise>

    suspend fun upsertExercise(exercise: Exercise)

    suspend fun removeExercise(primaryKey :String)

}