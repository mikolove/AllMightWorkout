package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.exercise.ExerciseSet

interface ExerciseSetNetworkDataSource {

    suspend fun upsertExerciseSet(exerciseSet: ExerciseSet, idExercise : String,idWorkout : String)

    suspend fun removeExerciseSetById(primaryKey :String, idExercise: String, idWorkout: String)
}