package com.mikolove.core.domain.workouttype

import com.mikolove.core.domain.bodypart.BodyPart
import java.util.Locale
import java.util.UUID

data class WorkoutType(
    var idWorkoutType: String = UUID.randomUUID().toString(),
    var name: String,
    var bodyParts : List<BodyPart> = listOf()
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutType

        if (idWorkoutType != other.idWorkoutType) return false
        if (name != other.name) return false
        if (bodyParts != other.bodyParts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idWorkoutType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (bodyParts.hashCode())
        return result
    }

    override fun toString(): String {
        return "${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"
    }
}
