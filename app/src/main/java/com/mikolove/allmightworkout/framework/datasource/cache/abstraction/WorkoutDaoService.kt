package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_PAGINATION_PAGE_SIZE

interface WorkoutDaoService {

    suspend fun insertWorkout(workout: Workout) : Long

    suspend fun updateWorkout(primaryKey: String, name : String, updatedAt : String, isActive : Boolean) : Int

    suspend fun removeWorkout(id :String) : Int

    suspend fun removeWorkouts(workouts : List<Workout>) : Int

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getTotalWorkout() : Int

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutsOrderByNameDESC(
        query: String,
        page: Int,
        pageSize: Int = WORKOUT_PAGINATION_PAGE_SIZE
    ): List<Workout>

    suspend fun getWorkoutsOrderByNameASC(
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