package com.mikolove.core.domain.exercise

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ExerciseSet(
    var idExerciseSet: String,
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var order : Int,
    var startedAt: String?,
    var endedAt: String?,
    var createdAt: String,
    var updatedAt: String,
) : Parcelable{



    fun start(date : String){
        startedAt = date
    }

    fun stop(date : String){
        endedAt = date
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseSet

        if (idExerciseSet != other.idExerciseSet) return false
        if (reps != other.reps) return false
        if (weight != other.weight) return false
        if (time != other.time) return false
        if (restTime != other.restTime) return false
        if (order != other.order) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idExerciseSet.hashCode()
        result = 31 * result + reps
        result = 31 * result + weight
        result = 31 * result + time
        result = 31 * result + restTime
        result = 31 * result + order
        result = 31 * result + (startedAt?.hashCode() ?: 0)
        result = 31 * result + (endedAt?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        return result
    }
}