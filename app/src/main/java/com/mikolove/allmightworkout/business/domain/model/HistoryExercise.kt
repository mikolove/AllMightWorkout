package com.mikolove.allmightworkout.business.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryExercise(
    var idHistoryExercise: String,
    var name: String,
    var bodyPart: String,
    var workoutType: String,
    var exerciseType: String,
    var historySets: List<HistoryExerciseSet>?,
    var startedAt: String,
    var endedAt: String,
    var createdAt: String,
    var updatedAt: String
) : Parcelable