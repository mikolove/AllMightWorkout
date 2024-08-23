package com.mikolove.core.domain.analytics

import java.time.ZonedDateTime
import java.util.*


class HistoryExerciseFactory{

    fun createHistoryExercise(
        idHistoryExercise: String = UUID.randomUUID().toString(),
        name : String,
        bodyPart: List<String>,
        workoutType : String,
        exerciseType : String,
        historySets : List<HistoryExerciseSet> = listOf(),
        startedAt : ZonedDateTime,
        endedAt : ZonedDateTime,
        createdAt : ZonedDateTime
    ) : HistoryExercise {
        
        return HistoryExercise(
            idHistoryExercise = idHistoryExercise,
            name = name ,
            bodyPart = bodyPart,
            workoutType = workoutType,
            historySets = historySets,
            exerciseType = exerciseType ,
            startedAt = startedAt,
            endedAt = endedAt ,
            createdAt = createdAt,
            )
    }

}