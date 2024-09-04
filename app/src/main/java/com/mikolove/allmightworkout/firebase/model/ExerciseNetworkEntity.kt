package com.mikolove.allmightworkout.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ExerciseNetworkEntity(
    @DocumentId
    var idExercise: String,
    var name: String,
    var bodyPartIds: List<String>,
    var exerciseType: String,
    //Boolean prefixed with is
    @field:JvmField
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {
    constructor() : this(
        "",
        "",
        listOf(),
        "",
        true,
        Timestamp.now(),
        Timestamp.now()
    )
}