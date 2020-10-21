package com.mikolove.allmightworkout.business.domain.data.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutCacheDataSource {

    suspend fun insertWorkout(workout: Workout) : Long

    suspend fun updateWorkout(workout : Workout) : Int

    suspend fun removeWorkout(id :Long) : Int

    suspend fun addExercises(exercises : List<Exercise>) : LongArray

    suspend fun removeExercises(exercises: List<Exercise>) : Int

    suspend fun getWorkout(query : String, filterAndOrder : String, page : Int) : List<Workout>

    suspend fun getWorkoutById(primaryKey : Long) : Workout?

    suspend fun getWorkoutTotalNumber() : Int
}