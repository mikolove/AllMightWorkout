package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutNetworkDataSource {

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(primaryKey: String, workout : Workout)

    suspend fun removeWorkout(primaryKey :String)

    suspend fun addExercises(exercises : List<Exercise>)

    suspend fun removeExercises(exercises: List<Exercise>)

    suspend fun getWorkout() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

}