package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class HistoryExerciseNetworkEntity(
    var idHistoryExercise: String,
    var name: String,
    var bodyPart: String,
    var workoutType: String,
    var exerciseType: String,
    var historySets: List<HistoryExerciseSetNetworkEntity>?,
    var startedAt: Timestamp,
    var endedAt: Timestamp,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {
}