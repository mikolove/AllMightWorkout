package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var idUser : String,
    var name : String,
    var email : String,
    var workouts : List<Workout>?,
    var createdAt: String,
    var updatedAt : String
) : Parcelable{

}
