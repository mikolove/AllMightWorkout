package com.mikolove.core.domain.exercise.abstraction

import com.mikolove.core.domain.exercise.ExerciseSet

interface ExerciseSetCacheDataSource {

    suspend fun upsertExerciseSet(exerciseSet: ExerciseSet, idExercise: String, idWorkout: String) : Long

    suspend fun removeExerciseSets(exerciseSets: List<ExerciseSet>,idExercise: String,idWorkout: String) : Int

    suspend fun getExerciseSetByIdExercise(idExercise: String, idWorkout: String): List<ExerciseSet>


}