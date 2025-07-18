package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.WorkoutCacheEntity
import com.mikolove.core.database.model.WorkoutWithExercisesCacheEntity
import com.mikolove.core.database.model.WorkoutWithExercisesWithGroupsCacheEntity
import com.mikolove.core.domain.workout.Workout

fun WorkoutCacheEntity.toWorkout() : Workout{
    return Workout(
        idWorkout = idWorkout,
        name = name,
        exercises = listOf(),
        groups = listOf(),
        isActive = isActive,
        startedAt = null,
        endedAt = null,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
fun WorkoutWithExercisesCacheEntity.toWorkout() : Workout{
    return Workout(
        idWorkout = workoutCacheEntity.idWorkout,
        name = workoutCacheEntity.name,
        exercises = listOfExerciseCacheEntity?.map { it.toExercise() } ?: listOf(),
        isActive = workoutCacheEntity.isActive,
        startedAt = null,
        endedAt = null,
        createdAt = workoutCacheEntity.createdAt,
        updatedAt = workoutCacheEntity.updatedAt
    )
}

fun WorkoutWithExercisesWithGroupsCacheEntity.toWorkout() : Workout{
    return Workout(
        idWorkout = workoutCacheEntity.idWorkout,
        name = workoutCacheEntity.name,
        exercises = listOfExerciseCacheEntity?.map { it.toExercise() } ?: listOf(),
        groups = listOfGroupCacheEntity?.map { it.toGroup() } ?: listOf(),
        isActive = workoutCacheEntity.isActive,
        startedAt = null,
        endedAt = null,
        createdAt = workoutCacheEntity.createdAt,
        updatedAt = workoutCacheEntity.updatedAt
    )
}

fun Workout.toWorkoutCacheEntity(idUser : String) : WorkoutCacheEntity{
    return WorkoutCacheEntity(
        idWorkout = idWorkout,
        name = name,
        isActive = isActive,
        idUser = idUser,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}