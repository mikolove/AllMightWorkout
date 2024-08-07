package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.core.domain.exercise.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseSetDaoService

class ExerciseSetCacheDataSourceImpl
constructor( private val exerciseSetDaoService : ExerciseSetDaoService)
    : ExerciseSetCacheDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet, idExercise: String): Long = exerciseSetDaoService.insertExerciseSet(exerciseSet,idExercise)

    override suspend fun updateExerciseSet(
        primaryKey: String,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int,
        order: Int,
        updatedAt: String,
        idExercise: String
    ): Int = exerciseSetDaoService.updateExerciseSet(
        primaryKey, reps, weight, time, restTime,order, updatedAt, idExercise
    )

    override suspend fun removeExerciseSets(exerciseSets: List<ExerciseSet>): Int = exerciseSetDaoService.removeExerciseSets(exerciseSets)

    override suspend fun removeExerciseSetById(primaryKey: String, idExercise: String): Int = exerciseSetDaoService.removeExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetById(primaryKey: String, idExercise: String): ExerciseSet? = exerciseSetDaoService.getExerciseSetById(primaryKey,idExercise)

    override suspend fun getExerciseSetByIdExercise(idExercise: String): List<ExerciseSet> = exerciseSetDaoService.getExerciseSetByIdExercise(idExercise)

    override suspend fun getTotalExercisesSetByExercise(
        idExercise: String
    ): Int = exerciseSetDaoService.getTotalExercisesSetByExercise(idExercise)
}
