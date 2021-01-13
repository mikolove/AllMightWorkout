package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.database.WORKOUT_PAGINATION_PAGE_SIZE

interface WorkoutDaoService {

    suspend fun insertWorkout(workout: Workout) : Long

    suspend fun updateWorkout(primaryKey: String, name : String, isActive : Boolean) : Int

    suspend fun removeWorkout(id :String) : Int

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getTotalWorkout() : Int

    suspend fun getWorkout() : List<Workout>

    suspend fun getWorkoutOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutOrderByNameASC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Workout>
}