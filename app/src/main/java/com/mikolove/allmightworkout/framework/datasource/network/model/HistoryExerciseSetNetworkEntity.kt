package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class HistoryExerciseSetNetworkEntity(
    var idHistoryExerciseSet: String,
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var startedAt: Timestamp,
    var endedAt: Timestamp,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {
}