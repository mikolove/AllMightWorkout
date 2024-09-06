package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.ExerciseCacheEntity
import com.mikolove.core.database.model.ExerciseWithSetsCacheEntity
import com.mikolove.core.domain.exercise.Exercise

fun Exercise.toExerciseCacheEntity(idUser : String) : ExerciseCacheEntity{
    return ExerciseCacheEntity(
        idExercise = idExercise,
        name = name,
        exerciseType = exerciseType,
        isActive = isActive,
        idUser = idUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ExerciseCacheEntity.toExercise() : Exercise{
    return Exercise(
        idExercise = idExercise,
        name = name,
        exerciseType = exerciseType,
        isActive = isActive,
        startedAt = null,
        endedAt = null,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ExerciseWithSetsCacheEntity.toExercise() : Exercise{
    return Exercise(
        idExercise = exercise.idExercise,
        name = exercise.name,
        exerciseType = exercise.exerciseType,
        sets = listOfExerciseSetCacheEntity.map { it.toExerciseSet() },
        bodyPart = listOfBodyPartsCacheEntity?.map { it.toBodyPart()} ?: listOf() ,
        isActive = exercise.isActive,
        startedAt = null,
        endedAt = null,
        createdAt = exercise.createdAt,
        updatedAt = exercise.updatedAt
    )
}