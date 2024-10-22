package com.mikolove.core.network.firebase.model

data class BodyPartExerciseNetworkEntity(
    var idBodyPart : String,
    var name : String
) {

    constructor() : this(
    "",
    ""
    )
}