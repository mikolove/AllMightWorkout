package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Workout(
    var idWorkout: Long,
    var name: String,
    var exercises: List<Exercise>?,
    var bodyGroups : List<BodyGroup>?,
    var isActive: Boolean,
    var created_at: String,
    var updated_at: String
) : Parcelable