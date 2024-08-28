package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkService
import com.mikolove.core.domain.workout.Workout
import java.time.ZonedDateTime


class WorkoutNetworkDataSourceImpl
constructor(private val workoutNetworkService : WorkoutNetworkService) :
    WorkoutNetworkDataSource {

    override suspend fun insertWorkout(workout: Workout) = workoutNetworkService.insertWorkout(workout)

    override suspend fun updateWorkout(workout: Workout) = workoutNetworkService.updateWorkout(workout)

    override suspend fun removeWorkout(primaryKey: String)  = workoutNetworkService.removeWorkout(primaryKey)

    override suspend fun getExerciseIdsUpdate(idWorkout: String): ZonedDateTime? = workoutNetworkService.getExerciseIdsUpdate(idWorkout)

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: ZonedDateTime?)= workoutNetworkService.updateExerciseIdsUpdatedAt(idWorkout,exerciseIdsUpdatedAt)

    override suspend fun getWorkouts(): List<Workout>  = workoutNetworkService.getWorkouts()

    override suspend fun getWorkoutById(primaryKey: String): Workout?  = workoutNetworkService.getWorkoutById(primaryKey)

    override suspend fun getWorkoutTotalNumber(): Int = workoutNetworkService.getWorkoutTotalNumber()

    override suspend fun getDeletedWorkouts(): List<Workout> = workoutNetworkService.getDeletedWorkouts()

    override suspend fun insertDeleteWorkout(workout: Workout) = workoutNetworkService.insertDeleteWorkout(workout)

    override suspend fun insertDeleteWorkouts(workouts: List<Workout>) = workoutNetworkService.insertDeleteWorkouts(workouts)
}
