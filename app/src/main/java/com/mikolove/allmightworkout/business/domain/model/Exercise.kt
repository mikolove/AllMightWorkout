package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Exercise(
    var idExercise: String,
    var name: String,
    var sets: List<ExerciseSet>,
    var bodyPart: BodyPart?,
    var exerciseType: ExerciseType,
    var isActive: Boolean,
    var startedAt: String?,
    var endedAt: String?,
    var createdAt: String,
    var updatedAt: String) : Parcelable{


    fun start(date : String){
        startedAt = date
    }

    fun stop(date : String){
        endedAt = date
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exercise

        if (idExercise != other.idExercise) return false
        if (name != other.name) return false
        if (sets != other.sets) return false
        if (bodyPart != other.bodyPart) return false
        if (exerciseType != other.exerciseType) return false
        if (isActive != other.isActive) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idExercise.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + sets.hashCode()
        result = 31 * result + (bodyPart?.hashCode() ?: 0)
        result = 31 * result + exerciseType.hashCode()
        result = 31 * result + isActive.hashCode()
        result = 31 * result + (startedAt?.hashCode() ?: 0)
        result = 31 * result + (endedAt?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        return result
    }
}

