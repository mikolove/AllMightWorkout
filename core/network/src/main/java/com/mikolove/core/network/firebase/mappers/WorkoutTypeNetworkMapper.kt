package com.mikolove.core.network.firebase.mappers

import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.network.firebase.model.WorkoutTypeNetworkEntity

fun WorkoutType.toWorkoutTypeNetworkEntity() : WorkoutTypeNetworkEntity{
    return WorkoutTypeNetworkEntity(
        idWorkoutType = this.idWorkoutType,
        name = this.name,
    )
}

fun WorkoutTypeNetworkEntity.toWorkoutType() : WorkoutType{
        return WorkoutType(
            idWorkoutType = this.idWorkoutType,
            name = this.name,
            bodyParts = this.bodyParts.map { it.toBodyPart() }
        )
}