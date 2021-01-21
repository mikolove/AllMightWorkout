package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class WorkoutType(
    var idWorkoutType: String,
    var name: String,
    var bodyParts : List<BodyPart>?
) : Parcelable
