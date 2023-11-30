package com.mikolove.allmightworkout.business.domain.model

import com.mikolove.allmightworkout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryExerciseFactory
@Inject
constructor(private val dateUtil: DateUtil){

    fun createHistoryExercise(
        idHistoryExercise: String?,
        name : String?,
        bodyPart: String?,
        workoutType : String?,
        exerciseType : String?,
        historySets : List<HistoryExerciseSet>?,
        started_at : String?,
        ended_at : String?,
        created_at : String?
    ) : HistoryExercise{
        
        return HistoryExercise(
            idHistoryExercise = idHistoryExercise ?: UUID.randomUUID().toString(),
            name = name ?: "New history exercise",
            bodyPart = bodyPart ?: "New bodypart",
            workoutType = workoutType?: "New workouttype",
            historySets = historySets ?: listOf(),
            exerciseType = exerciseType ?: "New history exercise",
            startedAt = started_at ?: dateUtil.getCurrentTimestamp(),
            endedAt = ended_at ?: dateUtil.getCurrentTimestamp(),
            createdAt = created_at?: dateUtil.getCurrentTimestamp(),
            updatedAt = dateUtil.getCurrentTimestamp())
    }

}