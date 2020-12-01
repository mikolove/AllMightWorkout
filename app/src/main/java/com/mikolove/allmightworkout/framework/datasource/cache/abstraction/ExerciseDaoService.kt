package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.framework.datasource.database.EXERCISE_PAGINATION_PAGE_SIZE

interface ExerciseDaoService {

    suspend fun insertExercise(exercise: Exercise) : Long

    suspend fun updateExercise( primaryKey: String, name: String, idBodyPart: String, isActive: Boolean, exerciseType: String) : Int

    suspend fun removeExerciseById(primaryKey :String) : Int

    suspend fun addSets(sets : List<ExerciseSet>) : LongArray

    suspend fun removeSets(sets : List<ExerciseSet>) : Int

    suspend fun getExerciseById(primaryKey: String) : Exercise?

    suspend fun getExercises() : List<Exercise>

    suspend fun getTotalExercises() : Int

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