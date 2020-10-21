package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ExerciseFactory
@Inject
constructor( private val dateUtil: DateUtil, private val exerciseSetFactory: ExerciseSetFactory){

    fun createExercise(
        idExercise : Long?,
        name : String?,
        sets : List<ExerciseSet>?,
        bodyPart: BodyPart,
        isActive : Boolean? ,
    ) : Exercise {
        return Exercise(
            idExercise = idExercise ?: 0L,
            name = name ?: "New exercise",
            sets =  sets ?: ArrayList(),
            bodyPart = bodyPart,
            isActive = isActive ?: true,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    //For testing purpose
    fun createExerciseList(numberOfExercise : Int) : List<Exercise>{
        val listOfExercise = ArrayList<Exercise>()
        for(i in 1..numberOfExercise){
            listOfExercise.add(
                createExercise(
                    idExercise = i.toLong(),
                    name = UUID.randomUUID().toString(),
                    sets = exerciseSetFactory.createListOfExerciseSet(4),
                    bodyPart = BodyPart.BICEPS,
                    isActive = true
                )
            )
        }
        return listOfExercise
    }

}