package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.BodyPart
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

    override suspend fun updateExercise(primaryKey: String, name: String, bodyPart: BodyPart, isActive: Boolean, exerciseType: String): Int =
        exerciseDaoService.updateExercise(primaryKey,name,bodyPart,isActive,exerciseType)

    override suspend fun removeExerciseById(primaryKey: String): Int = exerciseDaoService.removeExerciseById(primaryKey)

    override suspend fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise> {
        return exerciseDaoService.returnOrderedQuery(query,filterAndOrder,page)
    }

    override suspend fun getExerciseById(primaryKey: String): Exercise? = exerciseDaoService.getExerciseById(primaryKey)

    override suspend fun getTotalExercises(): Int = exerciseDaoService.getTotalExercises()
}
