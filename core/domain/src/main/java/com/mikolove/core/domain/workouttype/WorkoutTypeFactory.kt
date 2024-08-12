package com.mikolove.core.domain.workouttype

import com.mikolove.core.domain.bodypart.BodyPart
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutTypeFactory
@Inject
constructor()
{
    fun createWorkoutType(
        idWorkoutType : String?,
        name : String?,
        bodyParts : List<BodyPart>?
    ) : WorkoutType {
        return WorkoutType(
            idWorkoutType = idWorkoutType ?: UUID.randomUUID().toString(),
            name = name ?: "New bodyGroup",
            bodyParts = bodyParts
        )
    }

}