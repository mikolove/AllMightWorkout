package com.mikolove.core.database.mappers

import com.mikolove.core.database.model.HistoryExerciseCacheEntity
import com.mikolove.core.database.model.HistoryExerciseSetCacheEntity
import com.mikolove.core.database.model.HistoryExerciseWithSetsCacheEntity
import com.mikolove.core.database.model.HistoryWorkoutCacheEntity
import com.mikolove.core.database.model.HistoryWorkoutWithExercisesCacheEntity
import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryWorkout

fun HistoryWorkoutWithExercisesCacheEntity.toHistoryWorkout() : HistoryWorkout{
    return HistoryWorkout(
        idHistoryWorkout = historyWorkoutCacheEntity.idHistoryWorkout,
        name = historyWorkoutCacheEntity.name,
        historyExercises = listOfHistoryExercisesCacheEntity.map { it.toHistoryExercise() },
        startedAt = historyWorkoutCacheEntity.startedAt,
        endedAt = historyWorkoutCacheEntity.endedAt,
        createdAt = historyWorkoutCacheEntity.createdAt,
    )
}

fun HistoryWorkout.toHistoryWorkoutCacheEntity(idUser : String) : HistoryWorkoutCacheEntity{
    return HistoryWorkoutCacheEntity(
        idHistoryWorkout = idHistoryWorkout,
        name = name,
        idUser = idUser,
        startedAt = startedAt,
        endedAt = endedAt,
        createdAt = createdAt,
    )
}

fun HistoryExerciseWithSetsCacheEntity.toHistoryExercise() : HistoryExercise{
    return HistoryExercise(
        idHistoryExercise = historyExerciseCacheEntity.idHistoryExercise,
        name = historyExerciseCacheEntity.name,
        historySets = listOfHistoryExerciseSetsCacheEntity.map { it.toHistoryExerciseSet() },
        bodyPart = historyExerciseCacheEntity.bodyPart,
        workoutType = historyExerciseCacheEntity.exerciseType,
        exerciseType = historyExerciseCacheEntity.exerciseType,
        startedAt = historyExerciseCacheEntity.startedAt,
        endedAt = historyExerciseCacheEntity.endedAt,
        createdAt = historyExerciseCacheEntity.createdAt,
    )
}

fun HistoryExercise.toHistoryExerciseCacheEntity(idHistoryWorkout : String) : HistoryExerciseCacheEntity{
    return HistoryExerciseCacheEntity(
        idHistoryExercise = idHistoryExercise,
        idHistoryWorkout = idHistoryWorkout,
        name = name,
        bodyPart = bodyPart,
        workoutType = workoutType,
        exerciseType = exerciseType,
        startedAt = startedAt,
        endedAt = endedAt,
        createdAt = createdAt
    )
}

fun HistoryExerciseSetCacheEntity.toHistoryExerciseSet() : HistoryExerciseSet{
    return HistoryExerciseSet(
        idHistoryExerciseSet = idHistoryExerciseSet,
        reps = reps,
        weight = weight,
        time = time,
        restTime = restTime,
        startedAt = startedAt,
        endedAt = endedAt,
        createdAt = createdAt
    )
}

fun HistoryExerciseSet.toHistoryExerciseSetCacheEntity(idHistoryExercise : String) : HistoryExerciseSetCacheEntity{
    return HistoryExerciseSetCacheEntity(
        idHistoryExerciseSet = idHistoryExerciseSet,
        idHistoryExercise = idHistoryExercise,
        reps = reps,
        weight = weight,
        time = time,
        restTime = restTime,
        startedAt = startedAt,
        endedAt = endedAt,
         createdAt = createdAt
    )
}