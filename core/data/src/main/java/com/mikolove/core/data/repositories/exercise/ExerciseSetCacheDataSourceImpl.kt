package com.mikolove.core.data.repositories.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheService
import com.mikolove.core.domain.exercise.ExerciseSet

class ExerciseSetCacheDataSourceImpl
constructor( private val exerciseSetCacheService : ExerciseSetCacheService)
    : ExerciseSetCacheDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String): Long = exerciseSetCacheService.insertExerciseSet(exerciseSet,idExercise)

    override suspend fun updateExerciseSet(
        primaryKey: String,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int,
        order: Int,
        updatedAt: String,
        idExercise: String
    ): Int = exerciseSetCacheService.updateExerciseSet(
        primaryKey, reps, weight, time, restTime,order, updatedAt, idExercise
    )

    override suspend fun removeExerciseSets(exerciseSets: List<ExerciseSet>): Int = exerciseSetCacheService.removeExerciseSets(exerciseSets)

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String): Int = exerciseSetCacheService.removeExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? = exerciseSetCacheService.getExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet> = exerciseSetCacheService.getExerciseSetByIdExercise(idExercise)

    override suspend fun getTotalExercisesSetByExercise(
        idExercise: String
    ): Int = exerciseSetCacheService.getTotalExercisesSetByExercise(idExercise)
}
