package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Workout
import kotlinx.coroutines.flow.Flow


interface WorkoutCacheDataSource {

    suspend fun upsertWorkout(workout: Workout, idUser : String) : Long

    suspend fun removeWorkouts(workouts : List<Workout>) : Int

    suspend fun getWorkouts(idUser : String) : Flow<List<Workout>>

    suspend fun getWorkoutByWorkoutType(idWorkoutType : List<String>,idUser : String) : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout
}