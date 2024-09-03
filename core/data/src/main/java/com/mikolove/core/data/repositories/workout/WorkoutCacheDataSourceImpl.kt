package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import kotlinx.coroutines.flow.Flow

class WorkoutCacheDataSourceImpl
constructor(
    private val workoutCacheService : WorkoutCacheService
) : WorkoutCacheDataSource {
    override suspend fun upsertWorkout(workout: Workout, idUser : String ): Long = workoutCacheService.upsertWorkout(workout,idUser)

    override suspend fun getWorkouts(idUser: String): Flow<List<Workout>> = workoutCacheService.getWorkouts(idUser)

    override suspend fun removeWorkouts(workouts: List<Workout>): Int = workoutCacheService.removeWorkouts(workouts)

    override suspend fun getWorkoutByWorkoutType(idWorkoutType: List<String>,idUser: String): List<Workout> =workoutCacheService.getWorkoutByWorkoutType(idWorkoutType,idUser)

    override suspend fun getWorkoutById(primaryKey: String): Workout = workoutCacheService.getWorkoutById(primaryKey)

}
