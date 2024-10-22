package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.network.firebase.model.ExerciseSetNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime

fun ExerciseSetNetworkEntity.toExerciseSet() : ExerciseSet{
    return ExerciseSet(
        idExerciseSet = this.idExerciseSet,
        reps = this.reps,
        weight = this.weight,
        time = this.time,
        restTime = this.restTime,
        order = this.order,
        createdAt = this.createdAt.toZoneDateTime(),
        updatedAt = this.updatedAt.toZoneDateTime()
    )
}

fun ExerciseSet.toExerciseSetNetworkEntity() : ExerciseSetNetworkEntity{
    return ExerciseSetNetworkEntity(
        idExerciseSet = this.idExerciseSet,
        reps = this.reps,
        weight = this.weight,
        time = this.time,
        restTime = this.restTime,
        order = this.order,
        createdAt = this.createdAt.toFirebaseTimestamp(),
        updatedAt = this.updatedAt.toFirebaseTimestamp()
    )
}
