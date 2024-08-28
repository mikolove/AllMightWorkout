package com.mikolove.allmightworkout.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class HistoryExerciseNetworkEntity(
    @DocumentId
    var idHistoryExercise: String,
    var name: String,
    var bodyPart: List<String>,
    var workoutType: String,
    var exerciseType: String,
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
        Timestamp.now(),
        Timestamp.now(),
        Timestamp.now(),
    )
}