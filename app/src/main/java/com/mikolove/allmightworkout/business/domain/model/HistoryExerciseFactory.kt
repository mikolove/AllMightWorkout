package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class HistoryExerciseFactory
@Inject
constructor( private val dateUtil: DateUtil,
             private val exerciseFactory: ExerciseFactory,
             private val exerciseSetFactory: ExerciseSetFactory,
             private val bodyPartFactory: BodyPartFactory,
             private val historyExerciseSetFactory: HistoryExerciseSetFactory){

    fun createHistoryExercise(
        idHistoryExercise : Long?,
        exercise : Exercise
    ) : HistoryExercise {

        //Generate History set from exercise
        val listOfHistoryExerciseSet = ArrayList<HistoryExerciseSet>()
        for( i in 1..exercise.sets.size){
            listOfHistoryExerciseSet.add(
                historyExerciseSetFactory.createHistoryExerciseSet(
                    idHistoryExerciseSet = i.toLong(),
                    exerciseSet = exercise.sets[i]
                )
            )
        }

        return HistoryExercise(
            idHistoryExercise = idHistoryExercise ?: 0L,
            exercise = exercise,
            sets = listOfHistoryExerciseSet,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    //For testing purpose
    fun createExerciseList(numberOfExercise : Int) : List<HistoryExercise>{
        val listOfHistoryExercise = ArrayList<HistoryExercise>()
        for(i in 1..numberOfExercise){
            listOfHistoryExercise.add(
                createHistoryExercise(
                    idHistoryExercise = i.toLong(),
                    exercise = exerciseFactory.createExercise(
                        idExercise = i.toLong(),
                        name = null,
                        sets = exerciseSetFactory.createListOfExerciseSet(4),
                        bodyPart = bodyPartFactory.getBodyPart(),
                        isActive = true)
                )
            )
        }
        return listOfHistoryExercise
    }

}