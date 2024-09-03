package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Workout
import java.time.ZonedDateTime

interface WorkoutNetworkService{

    suspend fun upsertWorkout(workout: Workout)

    suspend fun removeWorkout(id :String)

    suspend fun getWorkouts() : List<Workout>
}