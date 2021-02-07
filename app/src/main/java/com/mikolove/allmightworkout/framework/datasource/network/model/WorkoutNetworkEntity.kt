package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class WorkoutNetworkEntity (
    @DocumentId
    var idWorkout: String,
    var name: String,
    var exerciseIds : List<String>?,
    var exerciseIdsUpdatedAt : Timestamp?,
    @field:JvmField
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp){

    constructor() : this(
        "",
        "",
        listOf(),
        null,
        true,
        Timestamp.now(),
        Timestamp.now()
    )

}