package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.WorkoutExerciseDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutExerciseCacheDataSourceImpl
@Inject
constructor(private val workoutExerciseDaoService : WorkoutExerciseDaoService)
    : WorkoutExerciseCacheDataSource {

    override suspend fun getExercisesFromWorkoutId(workoutId: String): List<Exercise>?
        = workoutExerciseDaoService.getExercisesFromWorkoutId(workoutId)

    override suspend fun getWorkoutsFromExerciseId(exerciseId: String): List<Workout>?
        = workoutExerciseDaoService.getWorkoutsFromExerciseId(exerciseId)

    override suspend fun addExerciseToWorkout(workoutId: String, exerciseId: String): Long
        = workoutExerciseDaoService.addExerciseToWorkout(workoutId , exerciseId)

    override suspend fun removeExerciseFromWorkout(workoutId: String, exerciseId: String): Int
        = workoutExerciseDaoService.removeExerciseFromWorkout(workoutId, exerciseId)
}