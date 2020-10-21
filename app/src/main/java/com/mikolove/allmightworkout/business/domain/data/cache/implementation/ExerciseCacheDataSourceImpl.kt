package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class ExerciseCacheDataSourceImpl
@Inject
constructor(private val exerciseDaoService : ExerciseDaoService)
    : ExerciseCacheDataSource {

    override suspend fun insertExercise(exercise: Exercise): Long = exerciseDaoService.insertExercise(exercise)

    override suspend fun updateExercise(exercise: Exercise): Int = exerciseDaoService.updateExercise(exercise)

    override suspend fun removeExerciseById(primaryKey: Long): Int = exerciseDaoService.removeExerciseById(primaryKey)

    override suspend fun addSets(sets: List<ExerciseSet>): LongArray = exerciseDaoService.addSets(sets)

    override suspend fun removeSets(sets: List<ExerciseSet>): Int = exerciseDaoService.removeSets(sets)

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> = exerciseDaoService.getExercises(query,filterAndOrder,page)

    override suspend fun getExerciseById(primaryKey: Long): Exercise? = exerciseDaoService.getExerciseById(primaryKey)
}*/
