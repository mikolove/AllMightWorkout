package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ExerciseNetworkEntity(
    @DocumentId
    var idExercise: String,
    var name: String,
    var sets: List<ExerciseSetNetworkEntity>?,
    var bodyPart: BodyPartExerciseNetworkEntity?,
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
        null,
        "",
        true,
        Timestamp.now(),
        Timestamp.now()

    )
}