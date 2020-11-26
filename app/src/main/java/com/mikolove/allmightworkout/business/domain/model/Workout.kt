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
) : Parcelable