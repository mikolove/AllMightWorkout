package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseCacheDataSourceImpl
@Inject
constructor(private val exerciseDaoService : ExerciseDaoService)
    : ExerciseCacheDataSource {

    override suspend fun insertExercise(exercise: Exercise): Long = exerciseDaoService.insertExercise(exercise)

    override suspend fun updateExercise(primaryKey: String, exercise: Exercise): Int = exerciseDaoService.updateExercise(primaryKey,exercise)

    override suspend fun removeExerciseById(primaryKey: String): Int = exerciseDaoService.removeExerciseById(primaryKey)

    override suspend fun addSets(sets: List<ExerciseSet>): LongArray = exerciseDaoService.addSets(sets)

    override suspend fun removeSets(sets: List<ExerciseSet>): Int = exerciseDaoService.removeSets(sets)

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> {
        //TODO implement search
        return listOf()
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseDaoService.getExerciseById(primaryKey)

    override suspend fun getTotalExercises(): Int = exerciseDaoService.getTotalExercises()
}
