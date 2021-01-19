package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryExerciseSet(
    var idHistoryExerciseSet: String,
    var reps: Int ,
    var weight: Int ,
    var time : Int,
    var restTime : Int,
    var started_at : String,
    var ended_at : String,
    var created_at: String,
    var updated_at: String
) : Parcelable