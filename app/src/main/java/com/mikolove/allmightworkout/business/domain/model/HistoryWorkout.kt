package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryWorkout(
    var idHistoryWorkout: Long ,
    var workout: Workout,
    var historyExercises: List<HistoryExercise>,
    var created_at: String,
    var updated_at: String
) : Parcelable