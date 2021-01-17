package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class HistoryExerciseFactory
@Inject
constructor(private val dateUtil: DateUtil, private val historyExerciseSetFactory: HistoryExerciseSetFactory){

    fun createHistoryExerciseFromExercise(
        idHistoryExercise : String?,
        exercise : Exercise,
        created_at: String?
    ) : HistoryExercise {

        //Generate History set from exercise
        val listOfHistoryExerciseSet = ArrayList<HistoryExerciseSet>()
        for( i in 1..exercise.sets.size){
            listOfHistoryExerciseSet.add(
                historyExerciseSetFactory.createHistoryExerciseSetFromExerciseSet(
                    idHistoryExerciseSet = null,
                    exerciseSet = exercise.sets[i],
                    created_at = null
                )
            )
        }

        return HistoryExercise(
            idHistoryExercise = idHistoryExercise ?: UUID.randomUUID().toString(),
            name = exercise.name,
            bodyPart = exercise.bodyPart.name,
            workoutType = exercise.bodyPart.workoutType.name,
            historySets = listOfHistoryExerciseSet,
            exerciseType = exercise.exerciseType,
            created_at = created_at?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    fun createHistoryExercise(
        idHistoryExercise: String,
        name : String,
        bodyPart: String,
        workoutType : String,
        exerciseType : String,
        historySets : List<HistoryExerciseSet>,
        created_at : String,
        updated_at : String
    ) : HistoryExercise{
        
        return HistoryExercise(
            idHistoryExercise = idHistoryExercise,
            name = name,
            bodyPart = bodyPart,
            workoutType = workoutType,
            historySets = historySets,
            exerciseType = ExerciseType.valueOf(exerciseType),
            created_at = created_at,
            updated_at = updated_at)
    }

}