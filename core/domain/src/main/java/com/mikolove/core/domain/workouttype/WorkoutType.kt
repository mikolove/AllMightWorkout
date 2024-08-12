package com.mikolove.core.domain.workouttype

import android.os.Parcelable
import com.mikolove.core.domain.bodypart.BodyPart
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class WorkoutType(
    var idWorkoutType: String,
    var name: String,
    var bodyParts : List<BodyPart>?
) : Parcelable{

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
        result = 31 * result + (bodyParts?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"
    }
}
