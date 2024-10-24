package com.mikolove.core.network.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.mikolove.core.domain.analytics.HistoryExerciseSet

data class HistoryExerciseNetworkEntity(
    @DocumentId
    var idHistoryExercise: String,
    var name: String,
    var bodyPart: List<String>,
    var workoutType: String,
    var exerciseType: String,
    var historySets : List<HistoryExerciseSetNetworkEntity>,
    var startedAt: Timestamp,
    var endedAt: Timestamp,
    var createdAt: Timestamp
) {

    constructor() : this(
        "",
        "",
        listOf(),
        "",
        "",
        listOf(),
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
    )
}