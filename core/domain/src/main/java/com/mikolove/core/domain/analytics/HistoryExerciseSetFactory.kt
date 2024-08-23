package com.mikolove.core.domain.analytics

import java.util.UUID


class HistoryExerciseSetFactory{

    fun  createHistoryExerciseSet(
        idHistoryExerciseSet : String = UUID.randomUUID().toString(),
        reps: Int ,
        weight: Int ,
        time : Int,
        restTime : Int,
        startedAt : String,
        endedAt : String,
        createdAt: String
    ) : HistoryExerciseSet {
        return HistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet,
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            startedAt = startedAt,
            endedAt = endedAt,
            createdAt = createdAt,
        )
    }
}