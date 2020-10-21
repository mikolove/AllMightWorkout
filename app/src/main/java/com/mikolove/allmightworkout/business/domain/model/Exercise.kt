package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exercise(
    var idExercise: Long,
    var name: String,
    var sets: List<ExerciseSet>,
    var bodyPart: BodyPart,
    var isActive: Boolean,
    var created_at: String,
    var updated_at: String) : Parcelable