package com.mikolove.core.domain.workout

import com.mikolove.core.domain.exercise.Exercise
import java.time.ZonedDateTime
import java.util.UUID

data class Workout(
    var idWorkout: String = UUID.randomUUID().toString(),
    var name: String,
    var exercises: List<Exercise> = listOf(),
    var isActive: Boolean = true,
    var startedAt: ZonedDateTime? = null,
    var endedAt: ZonedDateTime? = null,
    var groups: List<Group> = listOf(),
    var createdAt: ZonedDateTime = ZonedDateTime.now(),
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
) {

    override fun hashCode(): Int {
        var result = idWorkout.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (exercises.hashCode())
        result = 31 * result + isActive.hashCode()
        result = 31 * result + (groups.hashCode())
        result = 31 * result + (startedAt?.hashCode() ?: 0)
        result = 31 * result + (endedAt?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Workout

        if (idWorkout != other.idWorkout) return false
        if (name != other.name) return false
        if (exercises != other.exercises) return false
        if (groups != other.groups) return false
        if (isActive != other.isActive) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

}
