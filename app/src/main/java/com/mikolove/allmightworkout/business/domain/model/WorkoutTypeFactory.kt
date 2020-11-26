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
        name : String?
    ) : WorkoutType {
        return WorkoutType(
            idWorkoutType = idWorkoutType ?: UUID.randomUUID().toString(),
            name = name ?: "New bodyGroup"
        )
    }
    //For testing purpose
    fun getListOfWorkoutType(numberOfBodyGroup : Int) : List<WorkoutType>{

        val listOfWorkoutType : ArrayList<WorkoutType> = ArrayList()
        for (i in 0 until numberOfBodyGroup){
            listOfWorkoutType.add(
                createWorkoutType(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString())
            )
        }

        return listOfWorkoutType
    }
}