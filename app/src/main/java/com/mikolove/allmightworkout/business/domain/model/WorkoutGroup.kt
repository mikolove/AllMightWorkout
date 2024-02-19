package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutGroup (
    var idGroup : String,
    var name : String,
    var createdAt: String,
    var updatedAt: String
) : Parcelable{

}