package com.mikolove.allmightworkout.firebase.model

import com.google.firebase.firestore.DocumentId

data class WorkoutTypeNetworkEntity(
    @DocumentId
    var idWorkoutType : String,
    var name : String
) {

    constructor() : this(
        "",
        ""
    )
}