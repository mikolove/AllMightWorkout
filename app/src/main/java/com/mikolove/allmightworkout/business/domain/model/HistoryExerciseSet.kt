package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryExerciseSet(
    var idHistoryExerciseSet: Long,
    var exerciseSet: ExerciseSet,
    var created_at: String,
    var updated_at: String
) : Parcelable