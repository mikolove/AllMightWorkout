package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkService
import com.mikolove.core.domain.workout.Workout

class WorkoutNetworkDataSourceImpl
constructor(private val workoutNetworkService : WorkoutNetworkService) :
    WorkoutNetworkDataSource {

    override suspend fun upsertWorkout(workout: Workout) = workoutNetworkService.upsertWorkout(workout)

    override suspend fun removeWorkout(primaryKey: String)  = workoutNetworkService.removeWorkout(primaryKey)

    override suspend fun getWorkouts(): List<Workout>  = workoutNetworkService.getWorkouts()

}
