package com.mikolove.allmightworkout.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class WorkoutNetworkEntity (
    @DocumentId
    var idWorkout: String,
    var name: String,
    var exerciseIdWithSet : Map<String, List<ExerciseSetNetworkEntity>>,
    var groupIds : List<String>?,
    @field:JvmField
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp){

    constructor() : this(
        "",
        "",
        mapOf(),
        null,
        true,
        Timestamp.now(),
        Timestamp.now()
    )

}