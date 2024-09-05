package com.mikolove.core.domain.bodypart

import com.mikolove.core.domain.workouttype.WorkoutType
import java.util.UUID


class BodyPartFactory
{

    fun createBodyPart(
        idBodyPart : String = UUID.randomUUID().toString(),
        name : String,
        workoutType : WorkoutType
    ) : BodyPart {
        return BodyPart(
            idBodyPart = idBodyPart ,
            name = name,
            workoutType = workoutType
        )
    }

}