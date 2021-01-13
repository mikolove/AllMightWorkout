package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyPart(
    var idBodyPart: String,
    var name: String,
    var workoutType: WorkoutType) : Parcelable