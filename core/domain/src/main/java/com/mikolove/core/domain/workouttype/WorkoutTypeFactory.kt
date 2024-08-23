package com.mikolove.core.domain.workouttype

import com.mikolove.core.domain.bodypart.BodyPart
import java.util.UUID


class WorkoutTypeFactory {
    fun createWorkoutType(
        idWorkoutType : String = UUID.randomUUID().toString(),
        name : String,
        bodyParts : List<BodyPart> = listOf()
    ) : WorkoutType {
        return WorkoutType(
            idWorkoutType = idWorkoutType,
            name = name,
            bodyParts = bodyParts
        )
    }
}