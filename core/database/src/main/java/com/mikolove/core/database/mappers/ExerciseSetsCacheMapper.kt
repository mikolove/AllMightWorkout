package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.ExerciseSetCacheEntity
import com.mikolove.core.domain.exercise.ExerciseSet

fun ExerciseSet.toExerciseSetCacheEntity(idWorkout : String, idExercise : String) : ExerciseSetCacheEntity{
    return ExerciseSetCacheEntity(
        idExercise = idExercise,
        idWorkout = idWorkout,
        idExerciseSet = idExerciseSet,
        weight = weight,
        reps = reps,
        time = time,
        order = order,
        restTime = restTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ExerciseSetCacheEntity.toExerciseSet() : ExerciseSet{
    return ExerciseSet(
        idExerciseSet = idExerciseSet,
        weight = weight,
        reps = reps,
        time = time,
        order = order,
        restTime = restTime,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}