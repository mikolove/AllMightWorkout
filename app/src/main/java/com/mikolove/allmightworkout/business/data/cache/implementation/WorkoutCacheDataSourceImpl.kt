package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutDaoService

class WorkoutCacheDataSourceImpl
constructor(
    private val workoutDaoService : WorkoutDaoService
) : WorkoutCacheDataSource {
    override suspend fun insertWorkout(workout: Workout): Long = workoutDaoService.insertWorkout(workout)

    override suspend fun updateWorkout(
        primaryKey: String,
        name: String,
        isActive: Boolean,
        updatedAt: String
    ): Int = workoutDaoService.updateWorkout(primaryKey ,name, updatedAt, isActive)

    override suspend fun removeWorkout(primaryKey: String): Int = workoutDaoService.removeWorkout(primaryKey)

    override suspend fun getExerciseIdsUpdate(idWorkout: String): String? = workoutDaoService.getExerciseIdsUpdate(idWorkout)

    override suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, exerciseIdsUpdatedAt: String?): Int  = workoutDaoService.updateExerciseIdsUpdatedAt(
        idWorkout,
        exerciseIdsUpdatedAt
    )

    override suspend fun getWorkouts(query: String, filterAndOrder: String, page: Int): List<Workout> {

        return workoutDaoService.returnOrderedQuery(
            query,
            filterAndOrder,
            page
        )
    }

    override suspend fun removeWorkouts(workouts: List<Workout>): Int = workoutDaoService.removeWorkouts(workouts)

    override suspend fun getWorkoutById(primaryKey: String): Workout? = workoutDaoService.getWorkoutById(primaryKey)

    override suspend fun getTotalWorkout(): Int = workoutDaoService.getTotalWorkout()
}
