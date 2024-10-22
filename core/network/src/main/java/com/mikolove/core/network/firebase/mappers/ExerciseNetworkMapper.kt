package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.network.firebase.model.ExerciseNetworkEntity
import com.mikolove.core.network.util.toFirebaseTimestamp
import com.mikolove.core.network.util.toZoneDateTime

fun ExerciseNetworkEntity.toExercise(bodyParts : List<BodyPart>): Exercise {
    return Exercise(
        idExercise = this.idExercise,
        name = this.name,
        exerciseType = ExerciseType.valueOf(this.exerciseType),
        isActive = this.isActive,
        bodyPart = bodyParts,
        sets = this.sets.map { it.toExerciseSet() },
        createdAt = this.createdAt.toZoneDateTime(),
        updatedAt = this.updatedAt.toZoneDateTime()
    )
}

fun Exercise.toExerciseNetworkEntity(): ExerciseNetworkEntity {
    return ExerciseNetworkEntity(
        idExercise = this.idExercise,
        name = this.name,
        bodyPartIds = this.bodyPart.mapIndexed{_, bodyPart -> bodyPart.idBodyPart},
        sets = this.sets.map { it.toExerciseSetNetworkEntity() },
        exerciseType = this.exerciseType.name,
        isActive = this.isActive,
        createdAt = this.createdAt.toFirebaseTimestamp(),
        updatedAt = this.updatedAt.toFirebaseTimestamp()
    )
}