package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseSetFactory
@Inject
constructor(private val dateUtil: DateUtil) {

    fun createExerciseSet(idExerciseSet : String? ,
                  reps : Int?,
                  weight : Int?,
                  time : Int?,
                  restTime : Int?,
                  created_at : String?
    ) : ExerciseSet{

        return ExerciseSet(
            idExerciseSet = idExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps ?: 8,
            weight = weight ?: 5,
            time = time ?: 60,
            restTime = restTime ?: 45,
            startedAt = null,
            endedAt = null,
            createdAt = created_at ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp())
    }

}