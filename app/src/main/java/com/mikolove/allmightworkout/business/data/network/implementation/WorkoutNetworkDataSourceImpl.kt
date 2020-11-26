package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.WorkoutFirestoreService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkoutNetworkDataSourceImpl
@Inject
constructor(private val workoutFirestoreService : WorkoutFirestoreService) : WorkoutNetworkDataSource{

    override suspend fun insertWorkout(workout: Workout) = workoutFirestoreService.insertWorkout(workout)

    override suspend fun updateWorkout(primaryKey: String, workout: Workout) = workoutFirestoreService.updateWorkout(primaryKey,workout)

    override suspend fun removeWorkout(primaryKey: String)  = workoutFirestoreService.removeWorkout(primaryKey)

    override suspend fun addExercises(exercises: List<Exercise>)  = workoutFirestoreService.addExercises(exercises)

    override suspend fun removeExercises(exercises: List<Exercise>) = workoutFirestoreService.removeExercises(exercises)

    override suspend fun getWorkout(): List<Workout>  = workoutFirestoreService.getWorkout()

    override suspend fun getWorkoutById(primaryKey: String): Workout?  = workoutFirestoreService.getWorkoutById(primaryKey)

    override suspend fun getWorkoutTotalNumber(): Int = workoutFirestoreService.getWorkoutTotalNumber()
}
