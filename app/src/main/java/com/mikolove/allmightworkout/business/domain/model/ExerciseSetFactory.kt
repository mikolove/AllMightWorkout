package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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
            weight = weight ?: 0,
            time = time ?: 0,
            restTime = restTime ?: 45,
            started_at = null,
            ended_at = null,
            created_at = created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp())
    }

}