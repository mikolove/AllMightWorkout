package com.mikolove.allmightworkout.business.domain.data.cache.implementation

import com.mikolove.allmightworkout.business.domain.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import javax.inject.Inject
import javax.inject.Singleton

/*
@Singleton
class WorkoutCacheDataSourceImpl
@Inject
constructor(
    private val workoutDaoService : WorkoutDaoService
) : WorkoutCacheDataSource{
    override suspend fun insertWorkout(workout: Workout): Long = workoutDaoService.insertWorkout(workout)

    override suspend fun updateWorkout(workout: Workout): Int = workoutDaoService.updateWorkout(workout)

    override suspend fun removeWorkout(id: Long): Int = workoutDaoService.removeWorkout(id)

    override suspend fun addExercises(exercises: List<Exercise>): LongArray = workoutDaoService.addExercises(exercises)

    override suspend fun removeExercises(exercises: List<Exercise>): Int = workoutDaoService.removeExercises(exercises)

    override suspend fun getWorkout(query: String, filterAndOrder: String, page: Int): List<Workout> = workoutDaoService.getWorkout(query,filterAndOrder,page)

    override suspend fun getWorkoutById(primaryKey: Long): Workout? = workoutDaoService.getWorkoutById(primaryKey)

    override suspend fun getWorkoutTotalNumber(): Int = workoutDaoService.getWorkoutTotalNumber()
}*/
