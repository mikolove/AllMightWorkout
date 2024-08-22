package com.mikolove.core.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class HistoryExerciseSetNetworkEntity(
    @DocumentId
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

    constructor() : this(
        "",
        0,
        0,
        0,
        0,
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
    )
}