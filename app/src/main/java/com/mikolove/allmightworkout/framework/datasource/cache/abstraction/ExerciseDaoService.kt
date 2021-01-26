package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.database.EXERCISE_PAGINATION_PAGE_SIZE

interface ExerciseDaoService {

    suspend fun insertExercise(exercise: Exercise) : Long

    suspend fun updateExercise( primaryKey: String, name: String, bodyPart: BodyPart, isActive: Boolean, exerciseType: String) : Int

    suspend fun removeExerciseById(primaryKey :String) : Int

    suspend fun getExerciseById(primaryKey: String) : Exercise?

    suspend fun getTotalExercises() : Int

    suspend fun getExercisesByWorkout(idWorkout: String): List<Exercise>?

    suspend fun removeExercises(exercise: List<Exercise>)  : Int

    suspend fun getExercises() : List<Exercise>

    suspend fun getExercisesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE
    ): List<Exercise>

    suspend fun getExercisesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE
    ): List<Exercise>

    suspend fun getExercisesOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE
    ): List<Exercise>

    suspend fun getExercisesOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = EXERCISE_PAGINATION_PAGE_SIZE
    ): List<Exercise>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Exercise>
}