package com.mikolove.core.domain.workout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group (
    var idGroup : String,
    var name : String,
    var workouts : List<Workout>?,
    var createdAt: String,
    var updatedAt: String
) : Parcelable{

}