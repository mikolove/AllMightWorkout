package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.ExerciseSetDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseSetCacheDataSourceImpl
@Inject
constructor( private val exerciseSetDaoService : ExerciseSetDaoService)
    : ExerciseSetCacheDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long = exerciseSetDaoService.insertExerciseSet(exerciseSet)

    override suspend fun updateExerciseSet(primaryKey: String, exerciseSet: ExerciseSet): Int = exerciseSetDaoService.updateExerciseSet(primaryKey, exerciseSet)

    override suspend fun removeExerciseSetById(primaryKey: String): Int = exerciseSetDaoService.removeExerciseSetById(primaryKey)

    override suspend fun getExerciseSetsByExerciseId(idExercise: String): List<ExerciseSet> = exerciseSetDaoService.getExerciseSetsByExerciseId(idExercise)

    override suspend fun getTotalExerciseSet(): Int = exerciseSetDaoService.getTotalExerciseSet()

}
