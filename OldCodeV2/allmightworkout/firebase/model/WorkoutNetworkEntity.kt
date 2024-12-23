package com.mikolove.allmightworkout.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class WorkoutNetworkEntity (
    @DocumentId
    var idWorkout: String,
    var name: String,
    var exerciseIds : List<String>?,
    var exerciseIdsUpdatedAt : Timestamp?,
    var groupIds : List<String>?,
    @field:JvmField
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp){

    constructor() : this(
        "",
        "",
        listOf(),
        null,
        listOf(),
        true,
        Timestamp.now(),
        Timestamp.now()
    )

}