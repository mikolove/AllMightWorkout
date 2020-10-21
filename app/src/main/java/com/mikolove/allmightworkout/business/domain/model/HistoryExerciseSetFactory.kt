package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class HistoryExerciseSetFactory
@Inject
constructor(private val dateUtil: DateUtil, private val exerciseSetFactory: ExerciseSetFactory){

    fun  createHistoryExerciseSet(
        idHistoryExerciseSet : Long?,
        exerciseSet : ExerciseSet
    ) :  HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ?: 0L,
            exerciseSet = exerciseSet,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }

    //For testing purpose
    fun createListOfHistoryExerciseSet( numberOfHistoryExerciseSet : Int) : List<HistoryExerciseSet>{

        val listOfHistoryExerciseSet : ArrayList<HistoryExerciseSet> = ArrayList()

        for(i in 1..numberOfHistoryExerciseSet){
            listOfHistoryExerciseSet.add(
                createHistoryExerciseSet(
                    idHistoryExerciseSet = i.toLong(),
                    exerciseSet = exerciseSetFactory.createExerciseSet(idExerciseSet = i.toLong(), weight = 0, reps = 4)
                )
            )
        }
        return listOfHistoryExerciseSet
    }

}