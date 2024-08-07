package com.mikolove.core.domain.workout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Workout(
    var idWorkout: String,
    var name: String,
    var exercises: List<Exercise>?,
    var isActive: Boolean,
    var exerciseIdsUpdatedAt : String?,
    var startedAt: String?,
    var endedAt: String?,
    var groups: List<Group>?,
    var createdAt: String,
    var updatedAt: String
) : Parcelable{

    fun start(date : String){
        startedAt = date
    }

    fun stop(date : String){
        endedAt = date
    }



    override fun hashCode(): Int {
        var result = idWorkout.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (exercises?.hashCode() ?: 0)
        result = 31 * result + isActive.hashCode()
        result = 31 * result + (exerciseIdsUpdatedAt?.hashCode() ?: 0)
        result = 31 * result + (groups?.hashCode() ?: 0)
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
        if (exerciseIdsUpdatedAt != other.exerciseIdsUpdatedAt) return false
        if (startedAt != other.startedAt) return false
        if (endedAt != other.endedAt) return false
        if (createdAt != other.createdAt) return false

        return true
    }

}
