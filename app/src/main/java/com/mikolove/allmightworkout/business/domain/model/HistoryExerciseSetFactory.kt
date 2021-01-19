package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class HistoryExerciseSetFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun  createHistoryExerciseSet(
        idHistoryExerciseSet : String?,
        reps: Int? ,
        weight: Int? ,
        time : Int?,
        restTime : Int?,
        started_at: String?,
        ended_at: String?,
        created_at: String?
    ) :  HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps ?: 0,
            weight = weight ?: 0,
            time = time ?: 0,
            restTime = restTime ?: 0,
            started_at = started_at ?: dateUtil.getCurrentTimestamp(),
            ended_at = ended_at ?: dateUtil.getCurrentTimestamp(),
            created_at = created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }
}