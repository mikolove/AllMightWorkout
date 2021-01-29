package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryExerciseSet(
    var idHistoryExerciseSet: String,
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var startedAt: String,
    var endedAt: String,
    var createdAt: String,
    var updatedAt: String
) : Parcelable