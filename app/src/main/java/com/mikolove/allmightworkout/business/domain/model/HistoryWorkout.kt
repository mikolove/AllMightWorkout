package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryWorkout(
    var idHistoryWorkout: String ,
    var name : String,
    var historyExercises: List<HistoryExercise>,
    var created_at: String,
    var updated_at: String
) : Parcelable