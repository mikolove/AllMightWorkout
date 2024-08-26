package com.mikolove.core.domain.analytics

import java.time.ZonedDateTime
import java.util.UUID

data class HistoryExerciseSet(
    var idHistoryExerciseSet: String = UUID.randomUUID().toString(),
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var startedAt: ZonedDateTime,
    var endedAt: ZonedDateTime,
    var createdAt: ZonedDateTime,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryExerciseSet

        if (idHistoryExerciseSet != other.idHistoryExerciseSet) return false
        if (reps != other.reps) return false
        if (weight != other.weight) return false
        if (time != other.time) return false
        if (restTime != other.restTime) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idHistoryExerciseSet.hashCode()
        result = 31 * result + reps
        result = 31 * result + weight
        result = 31 * result + time
        result = 31 * result + restTime
        result = 31 * result + startedAt.hashCode()
        result = 31 * result + endedAt.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}