package com.mikolove.core.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class HistoryWorkoutNetworkEntity(
    @DocumentId
    var idHistoryWorkout: String,
    var name: String,
    var startedAt: Timestamp,
    var endedAt: Timestamp,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {

    constructor() : this(
        "",
        "",
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
    )
}