package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExerciseSet(
    var idExerciseSet: Long,
    var reps: Int ,
    var weight: Int ,
    var created_at : String,
    var updated_at : String,
) : Parcelable