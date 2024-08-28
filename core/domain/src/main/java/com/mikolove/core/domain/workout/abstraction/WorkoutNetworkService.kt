package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Workout
import java.time.ZonedDateTime

interface WorkoutNetworkService{

    suspend fun insertWorkout(workout: Workout)

    suspend fun updateWorkout(workout: Workout)

    suspend fun removeWorkout(id :String)

    suspend fun getExerciseIdsUpdate(idWorkout : String) : ZonedDateTime?

    suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: ZonedDateTime?)

    suspend fun getWorkouts() : List<Workout>

    suspend fun getWorkoutById(primaryKey : String) : Workout?

    suspend fun getWorkoutTotalNumber() : Int

     suspend fun getDeletedWorkouts(): List<Workout>

     suspend fun insertDeleteWorkout(workout: Workout)

     suspend fun insertDeleteWorkouts(workouts: List<Workout>)
}