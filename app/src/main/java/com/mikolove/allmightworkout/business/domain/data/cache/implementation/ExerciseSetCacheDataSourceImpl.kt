package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class ExerciseSetCacheDataSourceImpl
@Inject
constructor( private val exerciseSetDaoService : ExerciseSetDaoService)
    : ExerciseSetCacheDataSource {

    override suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long = exerciseSetDaoService.insertExerciseSet(exerciseSet)

    override suspend fun updateExerciseSet(exerciseSet: ExerciseSet): Int = exerciseSetDaoService.updateExerciseSet(exerciseSet)

    override suspend fun removeExerciseSetById(primaryKey: Long): Int = exerciseSetDaoService.removeExerciseSetById(primaryKey)

    override suspend fun getExerciseSetByExerciseId(primaryKey: Long): List<ExerciseSet> = exerciseSetDaoService.getExerciseSetByExerciseId(primaryKey)
}*/
