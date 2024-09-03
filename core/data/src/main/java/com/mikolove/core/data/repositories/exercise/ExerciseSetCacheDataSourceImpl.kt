package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheService

class ExerciseSetCacheDataSourceImpl
constructor( private val exerciseSetCacheService : ExerciseSetCacheService)
    : ExerciseSetCacheDataSource {
    override suspend fun upsertExerciseSet(
        exerciseSet: ExerciseSet,
        idExercise: String,
        idWorkout: String
    ): Long = exerciseSetCacheService.upsertExerciseSet(exerciseSet,idExercise,idWorkout)

    override suspend fun removeExerciseSets(
        exerciseSets: List<ExerciseSet>,
        idExercise: String,
        idWorkout: String
    ): Int = exerciseSetCacheService.removeExerciseSets(exerciseSets,idExercise,idWorkout)

    override suspend fun getExerciseSetByIdExercise(
        idExercise: String,
        idWorkout: String
    ): List<ExerciseSet> = exerciseSetCacheService.getExerciseSetByIdExercise(idExercise,idWorkout)
}
