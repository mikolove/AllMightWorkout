package com.mikolove.allmightworkout.business.domain.model

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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