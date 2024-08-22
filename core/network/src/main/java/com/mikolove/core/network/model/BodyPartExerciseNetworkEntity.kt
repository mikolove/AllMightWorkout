package com.mikolove.core.network.model

data class BodyPartExerciseNetworkEntity(
    var idBodyPart : String,
    var name : String
) {

    constructor() : this(
    "",
    ""
    )
}