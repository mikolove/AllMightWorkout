package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseSetFactory
@Inject
constructor(private val dateUtil: DateUtil) {

    fun createExerciseSet(idExerciseSet : Long? ,
                  reps : Int?,
                  weight : Int? ,
    ) : ExerciseSet{

        return ExerciseSet(
            idExerciseSet = idExerciseSet ?: 0L,
            reps = reps ?: 8,
            weight = weight ?: 0,
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp())
    }

    fun createListOfExerciseSet(
        numberOfSet : Int = 4
    ) : List<ExerciseSet>{
        val listOfExerciseSet = ArrayList<ExerciseSet>()

        for(i in 1..numberOfSet){
            listOfExerciseSet.add(createExerciseSet(
                idExerciseSet = i.toLong(),
                reps = 8,
                weight = 0
            ))
        }

        return listOfExerciseSet
    }

}