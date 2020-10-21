package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HistoryExercise(
    var idHistoryExercise : Long,
    var exercise: Exercise,
    var sets: List<HistoryExerciseSet>,
    var created_at: String,
    var updated_at: String
) : Parcelable