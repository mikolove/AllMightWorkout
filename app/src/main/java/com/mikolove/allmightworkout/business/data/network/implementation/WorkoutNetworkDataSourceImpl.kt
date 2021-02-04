package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import javax.inject.Inject
import javax.inject.Singleton


class WorkoutNetworkDataSourceImpl
constructor(private val workoutFirestoreService : WorkoutFirestoreService) : WorkoutNetworkDataSource{

    override suspend fun insertWorkout(workout: Workout) = workoutFirestoreService.insertWorkout(workout)

    override suspend fun updateWorkout(workout: Workout) = workoutFirestoreService.updateWorkout(workout)

    override suspend fun removeWorkout(primaryKey: String)  = workoutFirestoreService.removeWorkout(primaryKey)

    override suspend fun getWorkouts(): List<Workout>  = workoutFirestoreService.getWorkouts()

    override suspend fun getWorkoutById(primaryKey: String): Workout?  = workoutFirestoreService.getWorkoutById(primaryKey)

    override suspend fun getWorkoutTotalNumber(): Int = workoutFirestoreService.getWorkoutTotalNumber()

    override suspend fun getDeletedWorkouts(): List<Workout> = workoutFirestoreService.getDeletedWorkouts()

    override suspend fun insertDeleteWorkout(workout: Workout) = workoutFirestoreService.insertDeleteWorkout(workout)

    override suspend fun insertDeleteWorkouts(workouts: List<Workout>) = workoutFirestoreService.insertDeleteWorkouts(workouts)
}
