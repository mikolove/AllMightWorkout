package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ExerciseSet(
    var idExerciseSet: String,
    var reps: Int ,
    var weight: Int ,
    var time : Int,
    var restTime : Int,
    var isWeightSet : Boolean,
    var created_at : String,
    var updated_at : String,
) : Parcelable