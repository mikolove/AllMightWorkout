package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class WorkoutFactory
@Inject
constructor( private val dateUtil: DateUtil ,
             private val exerciseFactory: ExerciseFactory){

    fun createWorkout(
        idWorkout : String? ,
        name : String?,
        exercises : List<Exercise>?,
        isActive : Boolean? = true,
        created_at : String?
    ) : Workout {
        return Workout(
            idWorkout = idWorkout ?: UUID.randomUUID().toString(),
            name = name ?: "New workout",
            exercises = exercises ,
            isActive = isActive ?: true,
            created_at = created_at?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    //For testing purpose
    fun createListOfWorkout( numberOfWorkout : Int){

        val listOfWorkout : ArrayList<Workout> = ArrayList()

        for(i in 0 until numberOfWorkout){
            listOfWorkout.add(
                createWorkout(
                    idWorkout = UUID.randomUUID().toString(),
                    name = UUID.randomUUID().toString(),
                    exercises = exerciseFactory.createExerciseList(2),
                    isActive = true,
                    created_at = dateUtil.getCurrentTimestamp()
                )
            )
        }
    }

}