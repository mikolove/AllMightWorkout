package com.mikolove.allmightworkout.business.domain.model

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class BodyPartFactory
@Inject
constructor(private val workoutTypeFactory: WorkoutTypeFactory)
{

    fun createBodyPart(
        idBodyPart : String?,
        name : String?,
        workoutType : WorkoutType
    ) : BodyPart {
        return BodyPart(
            idBodyPart = idBodyPart ?: "",
            name = name ?: "New bodyPart",
            workoutType = workoutType
        )
    }

    //For testing purpose
    fun getListOfBodyPart(numberOfBodyPart : Int, workoutType: WorkoutType? = null) : List<BodyPart>{
        val listOfBodyPart : ArrayList<BodyPart> = ArrayList()
        for (i in 0 until numberOfBodyPart){
            listOfBodyPart.add(
                createBodyPart(
                    i.toString(),
                    UUID.randomUUID().toString(),
                    workoutType = workoutType ?: workoutTypeFactory.createWorkoutType(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString()
                    ))
            )
        }

        return listOfBodyPart
    }

}