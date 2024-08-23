package com.mikolove.core.domain.analytics

import java.time.ZonedDateTime
import java.util.UUID

data class HistoryExercise(
    var idHistoryExercise: String = UUID.randomUUID().toString(),
    var name: String,
    var bodyPart: List<String> = listOf(),
    var workoutType: String,
    var exerciseType: String,
    var historySets: List<HistoryExerciseSet> = listOf(),
    var startedAt: ZonedDateTime,
    var endedAt: ZonedDateTime,
    var createdAt: ZonedDateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryExercise

        if (idHistoryExercise != other.idHistoryExercise) return false
        if (name != other.name) return false
        if (bodyPart != other.bodyPart) return false
        if (workoutType != other.workoutType) return false
        if (exerciseType != other.exerciseType) return false
        if (historySets != other.historySets) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idHistoryExercise.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + bodyPart.hashCode()
        result = 31 * result + workoutType.hashCode()
        result = 31 * result + exerciseType.hashCode()
        result = 31 * result + (historySets.hashCode())
        result = 31 * result + startedAt.hashCode()
        result = 31 * result + endedAt.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}