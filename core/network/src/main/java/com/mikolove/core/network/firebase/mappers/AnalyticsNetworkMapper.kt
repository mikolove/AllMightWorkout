package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.analytics.HistoryExercise
import com.mikolove.core.domain.analytics.HistoryExerciseSet
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.network.firebase.model.HistoryExerciseNetworkEntity
import com.mikolove.core.network.firebase.model.HistoryExerciseSetNetworkEntity
import com.mikolove.core.network.firebase.model.HistoryWorkoutNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime

fun HistoryWorkout.toHistoryWorkoutNetworkEntity() : HistoryWorkoutNetworkEntity{
    return HistoryWorkoutNetworkEntity(
        idHistoryWorkout = this.idHistoryWorkout,
        name = this.name,
        historyExercises = this.historyExercises.map { it.toHistoryExerciseNetworkEntity() },
        startedAt = this.startedAt.toFirebaseTimestamp(),
        endedAt = this.endedAt.toFirebaseTimestamp(),
        createdAt = this.createdAt.toFirebaseTimestamp()
    )
}

fun HistoryWorkoutNetworkEntity.toHistoryWorkout() : HistoryWorkout{
    return HistoryWorkout(
        idHistoryWorkout = this.idHistoryWorkout,
        name = this.name,
        historyExercises = this.historyExercises.map { it.toHistoryExercise() },
        startedAt = this.startedAt.toZoneDateTime(),
        endedAt = this.endedAt.toZoneDateTime(),
        createdAt = this.createdAt.toZoneDateTime()
    )
}

fun HistoryExerciseNetworkEntity.toHistoryExercise() : HistoryExercise {
    return HistoryExercise(
        idHistoryExercise = this.idHistoryExercise,
        name = this.name,
       bodyPart = this.bodyPart,
        workoutType = this.workoutType,
        exerciseType = this.exerciseType,
        historySets = this.historySets.map { it.toHistoryExerciseSet() },
        startedAt = this.startedAt.toZoneDateTime(),
        endedAt = this.endedAt.toZoneDateTime(),
        createdAt = this.createdAt.toZoneDateTime()
    )
}

fun HistoryExercise.toHistoryExerciseNetworkEntity() : HistoryExerciseNetworkEntity{
    return HistoryExerciseNetworkEntity(
        idHistoryExercise = this.idHistoryExercise,
        name = this.name,
        bodyPart = this.bodyPart,
        workoutType = this.workoutType,
        exerciseType = this.exerciseType,
        historySets = this.historySets.map { it.toHistoryExerciseSetNetworkEntity() },
        startedAt = this.startedAt.toFirebaseTimestamp(),
        endedAt = this.endedAt.toFirebaseTimestamp(),
        createdAt = this.createdAt.toFirebaseTimestamp()
    )
}

fun HistoryExerciseSet.toHistoryExerciseSetNetworkEntity() : HistoryExerciseSetNetworkEntity {
    return HistoryExerciseSetNetworkEntity(
        idHistoryExerciseSet = this.idHistoryExerciseSet,
        reps = this.reps,
        weight = this.weight,
        time = this.time,
        restTime = this.restTime,
        startedAt = this.startedAt.toFirebaseTimestamp(),
        endedAt = this.endedAt.toFirebaseTimestamp(),
        createdAt = this.createdAt.toFirebaseTimestamp()
    )
}

fun HistoryExerciseSetNetworkEntity.toHistoryExerciseSet() : HistoryExerciseSet {
    return HistoryExerciseSet(
        idHistoryExerciseSet = this.idHistoryExerciseSet,
        reps = this.reps,
        weight = this.weight,
        time = this.time,
        restTime = this.restTime,
        startedAt = this.startedAt.toZoneDateTime(),
        endedAt = this.endedAt.toZoneDateTime(),
        createdAt = this.createdAt.toZoneDateTime()
    )
}