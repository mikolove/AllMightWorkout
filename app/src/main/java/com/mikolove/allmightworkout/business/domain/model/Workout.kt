package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Workout(
    var idWorkout: String,
    var name: String,
    var exercises: List<Exercise>?,
    var isActive: Boolean,
    var startedAt: String?,
    var endedAt: String?,
    var createdAt: String,
    var updatedAt: String
) : Parcelable{

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Workout

        if(idWorkout != other.idWorkout) return false
        if(name != other.name) return false
        if(isActive != other.isActive) return false
        if(createdAt != other.createdAt) return false

        return true
    }

    fun start(date : String){
        startedAt = date
    }

    fun stop(date : String){
        endedAt = date
    }

}
