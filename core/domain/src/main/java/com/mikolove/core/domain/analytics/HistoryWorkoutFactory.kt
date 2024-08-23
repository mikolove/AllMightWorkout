package com.mikolove.core.domain.analytics

import java.time.ZonedDateTime
import java.util.UUID


class HistoryWorkoutFactory {

    fun createHistoryWorkout(
        idHistoryWorkout: String = UUID.randomUUID().toString(),
        name: String,
        historyExercises : List<HistoryExercise> = listOf(),
        startedAt : ZonedDateTime,
        endedAt : ZonedDateTime,
        createdAt: ZonedDateTime
    ): HistoryWorkout {
        return HistoryWorkout(
            idHistoryWorkout = idHistoryWorkout,
            name = name ,
            historyExercises = historyExercises,
            startedAt = startedAt ,
            endedAt = endedAt ,
            createdAt = createdAt ,
            )
    }

}