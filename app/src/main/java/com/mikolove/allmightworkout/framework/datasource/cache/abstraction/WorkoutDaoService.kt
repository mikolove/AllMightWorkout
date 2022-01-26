package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_PAGINATION_PAGE_SIZE

interface WorkoutDaoService {

    suspend fun insertWorkout(workout: Workout,idUser : String) : Long

    suspend fun updateWorkout(primaryKey: String, name : String, updatedAt : String, isActive : Boolean) : Int

    suspend fun removeWorkout(id :String) : Int

    suspend fun removeWorkouts(workouts : List<Workout>) : Int

    suspend fun getExerciseIdsUpdate(idWorkout : String) : String?

    suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?) : Int

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getTotalWorkout(idUser : String) : Int

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutsOrderByDateDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout>

    suspend fun getWorkoutsOrderByDateASC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout>

    suspend fun getWorkoutsOrderByNameDESC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout>

    suspend fun getWorkoutsOrderByNameASC(
        query: String,
        page: Int,
        idUser: String
    ): List<Workout>

    suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int,
        idUser : String
    ): List<Workout>
}