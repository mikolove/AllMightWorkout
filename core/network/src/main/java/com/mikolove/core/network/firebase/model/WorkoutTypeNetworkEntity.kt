package com.mikolove.core.network.firebase.model

import com.google.firebase.firestore.DocumentId

data class WorkoutTypeNetworkEntity(
    @DocumentId
    var idWorkoutType : String,
    var name : String,
    var bodyParts : List<BodyPartNetworkEntity> = emptyList()
) {

    constructor() : this(
        "",
        "",
        listOf()
    )
}