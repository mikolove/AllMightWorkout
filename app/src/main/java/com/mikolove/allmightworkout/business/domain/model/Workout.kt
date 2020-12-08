package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Workout(
    var idWorkout: String,
    var name: String,
    var exercises: List<Exercise>?,
    var isActive: Boolean,
    var created_at: String,
    var updated_at: String
) : Parcelable{

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Workout

        if(idWorkout != other.idWorkout) return false
        if(name != other.name) return false
        if(isActive != other.isActive) return false
        if(created_at != other.created_at) return false

        return true
    }
}