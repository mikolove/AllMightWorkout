package com.mikolove.core.domain.analytics

import java.time.ZonedDateTime
import java.util.UUID

data class HistoryWorkout(
    var idHistoryWorkout: String = UUID.randomUUID().toString(),
    var name: String,
    var historyExercises: List<HistoryExercise> = listOf(),
    var startedAt: ZonedDateTime,
    var endedAt: ZonedDateTime,
    var createdAt: ZonedDateTime,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryWorkout

        if (idHistoryWorkout != other.idHistoryWorkout) return false
        if (name != other.name) return false
        if (historyExercises != other.historyExercises) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idHistoryWorkout.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (historyExercises?.hashCode() ?: 0)
        result = 31 * result + startedAt.hashCode()
        result = 31 * result + endedAt.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}