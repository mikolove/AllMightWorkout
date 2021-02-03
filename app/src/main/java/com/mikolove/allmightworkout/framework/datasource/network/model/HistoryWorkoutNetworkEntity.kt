package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class HistoryWorkoutNetworkEntity(
    var idHistoryWorkout: String,
    var name: String,
    var historyExercises: List<HistoryExerciseNetworkEntity>?,
    var startedAt: Timestamp,
    var endedAt: Timestamp,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {
}