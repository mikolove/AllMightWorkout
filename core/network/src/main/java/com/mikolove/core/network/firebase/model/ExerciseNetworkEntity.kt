package com.mikolove.core.network.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ExerciseNetworkEntity(
    @DocumentId
    var idExercise: String,
    var name: String,
    var bodyPartIds: List<String>,
    //var sets : List<ExerciseSetNetworkEntity>,
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
        //listOf(),
        "",
        true,
        Timestamp.now(),
        Timestamp.now()
    )
}