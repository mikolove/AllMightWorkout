package com.mikolove.allmightworkout.firebase.model

data class BodyPartExerciseNetworkEntity(
    var idBodyPart : String,
    var name : String
) {

    constructor() : this(
    "",
    ""
    )
}