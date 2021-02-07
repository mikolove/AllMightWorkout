package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutCacheDataSource {

    suspend fun insertWorkout(workout: Workout) : Long

    suspend fun updateWorkout(
        primaryKey: String,
        name: String,
        isActive: Boolean,
        updatedAt: String
    ) : Int

    suspend fun removeWorkout(primaryKey:String) : Int

    suspend fun removeWorkouts(workouts : List<Workout>) : Int

    suspend fun getExerciseIdsUpdate(idWorkout : String) : String?

    suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?) : Int

    suspend fun getWorkouts(query : String, filterAndOrder : String, page : Int) : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getTotalWorkout() : Int
}