package com.mikolove.allmightworkout.framework.datasource.network.model

data class BodyPartExerciseNetworkEntity(
    var idBodyPart : String,
    var name : String
) {

    constructor() : this(
    "",
    ""
    )
}