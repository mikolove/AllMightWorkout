package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import com.mikolove.core.domain.workout.Workout
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var idUser : String,
    var name : String? = null,
    var email : String? = null,
    var workouts : List<Workout>? = null,
    var createdAt: String,
    var updatedAt : String
) : Parcelable{

}
