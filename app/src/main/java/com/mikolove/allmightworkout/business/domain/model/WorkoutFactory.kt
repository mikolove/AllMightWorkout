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
             private val exerciseFactory: ExerciseFactory,
             private val bodyGroupFactory: BodyGroupFactory){

    fun createWorkout(
        idWorkout : Long? ,
        name : String?,
        exercises : List<Exercise>?,
        bodyGroups: List<BodyGroup>?,
        isActive : Boolean?
    ) : Workout {
        return Workout(
            idWorkout = idWorkout ?: 0L,
            name = name ?: "New workout",
            exercises = exercises ?: null,
            bodyGroups = bodyGroups ?: null,
            isActive = isActive ?: true,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    //For testing purpose
    fun createListOfWorkout( numberOfWorkout : Int){

        val listOfWorkout : ArrayList<Workout> = ArrayList()

        for(i in 0 until numberOfWorkout){
            listOfWorkout.add(
                createWorkout(
                    idWorkout = i.toLong(),
                    name = UUID.randomUUID().toString(),
                    exercises = exerciseFactory.createExerciseList(2),
                    bodyGroups = bodyGroupFactory.getListOfBodyGroup(3),
                    isActive = true
                )
            )
        }
    }

}