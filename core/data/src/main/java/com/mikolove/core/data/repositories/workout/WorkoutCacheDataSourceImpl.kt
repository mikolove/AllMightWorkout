package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import com.mikolove.core.domain.workout.Workout

class WorkoutCacheDataSourceImpl
constructor(
    private val workoutCacheService : WorkoutCacheService
) : WorkoutCacheDataSource {
    override suspend fun insertWorkout(workout: Workout, idUser : String ): Long = workoutCacheService.insertWorkout(workout,idUser)

    override suspend fun updateWorkout(
        primaryKey: String,
        name: String,
        isActive: Boolean,
        updatedAt: String
    ): Int = workoutCacheService.updateWorkout(primaryKey ,name, updatedAt, isActive)

    override suspend fun removeWorkout(primaryKey: String): Int = workoutCacheService.removeWorkout(primaryKey)

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? = workoutCacheService.getExerciseIdsUpdate(idWorkout)

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?): Int  = workoutCacheService.updateExerciseIdsUpdatedAt(
        idWorkout,
        exerciseIdsUpdatedAt
    )

    override suspend fun getWorkouts(query: String, filterAndOrder: String, page: Int, idUser: String): List<Workout> {

        return workoutCacheService.returnOrderedQuery(
            query,
            filterAndOrder,
            page,
            idUser
        )
    }

    override suspend fun removeWorkouts(workouts: List<Workout>): Int = workoutCacheService.removeWorkouts(workouts)

    override suspend fun getWorkoutById(primaryKey: String): Workout? = workoutCacheService.getWorkoutById(primaryKey)

    override suspend fun getTotalWorkout(idUser: String): Int = workoutCacheService.getTotalWorkout(idUser)
}
