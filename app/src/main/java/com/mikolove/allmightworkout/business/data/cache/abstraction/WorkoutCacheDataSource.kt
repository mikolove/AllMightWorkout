package com.mikolove.allmightworkout.business.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutCacheDataSource {

    suspend fun insertWorkout(workout: Workout) : Long

    suspend fun updateWorkout(primaryKey: String, name : String, isActive : Boolean) : Int

    suspend fun removeWorkout(primaryKey :String) : Int

    suspend fun getWorkouts(query : String, filterAndOrder : String, page : Int) : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getTotalWorkout() : Int
}