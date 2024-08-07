package com.mikolove.core.domain.analytics

import com.mikolove.core.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


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
    ) : HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps ?: 0,
            weight = weight ?: 0,
            time = time ?: 0,
            restTime = restTime ?: 0,
            startedAt = started_at ?: dateUtil.getCurrentTimestamp(),
            endedAt = ended_at ?: dateUtil.getCurrentTimestamp(),
            createdAt = created_at ?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp()
        )
    }
}