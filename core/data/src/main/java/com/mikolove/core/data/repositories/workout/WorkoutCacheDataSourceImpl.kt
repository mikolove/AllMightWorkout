package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import kotlinx.coroutines.flow.Flow

class WorkoutCacheDataSourceImpl
constructor(
    private val workoutCacheService : WorkoutCacheService,

) : WorkoutCacheDataSource {
    override suspend fun upsertWorkout(workout: Workout, idUser : String ): Long = workoutCacheService.upsertWorkout(workout,idUser)

    override fun getWorkouts(idUser: String): Flow<List<Workout>> = workoutCacheService.getWorkouts(idUser)

    override suspend fun removeWorkouts(workouts: List<Workout>): Int = workoutCacheService.removeWorkouts(workouts)

    override suspend fun getWorkoutByWorkoutType(idWorkoutType: List<String>,idUser: String): List<Workout> =workoutCacheService.getWorkoutByWorkoutType(idWorkoutType,idUser)

    override suspend fun getWorkoutById(primaryKey: String): Workout = workoutCacheService.getWorkoutById(primaryKey)

    override suspend fun isExerciseInWorkout(idWorkout: String, idExercise: String): Boolean = workoutCacheService.isExerciseInWorkout(idWorkout,idExercise)

    override suspend fun addExerciseToWorkout(
        workoutId: String,
        exerciseId: String,
        exerciseSet: ExerciseSet
    ): Long = workoutCacheService.addExerciseToWorkout(workoutId,exerciseId,exerciseSet)

    override suspend fun removeExerciseFromWorkout(
        workoutId: String,
        exerciseId: String,
        sets: List<ExerciseSet>
    ): Int = workoutCacheService.removeExerciseFromWorkout(workoutId,exerciseId,sets)

    override suspend fun isWorkoutInGroup(idWorkout: String, idGroup: String): Boolean = workoutCacheService.isWorkoutInGroup(idWorkout,idGroup)

    override suspend fun addWorkoutToGroup(workoutId: String, groupId: String): Long = workoutCacheService.addWorkoutToGroup(workoutId,groupId)

    override suspend fun removeWorkoutFromGroup(workoutId: String, groupId: String): Int =workoutCacheService.removeWorkoutFromGroup(workoutId,groupId)
}
