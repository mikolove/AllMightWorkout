package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout

interface WorkoutNetworkDataSource {

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)

    suspend fun removeWorkout(primaryKey :String)

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

    suspend fun insertDeleteWorkout(workout: Workout)

    suspend fun insertDeleteWorkouts(workouts: List<Workout>)

    suspend fun getDeletedWorkouts() : List<Workout>

}