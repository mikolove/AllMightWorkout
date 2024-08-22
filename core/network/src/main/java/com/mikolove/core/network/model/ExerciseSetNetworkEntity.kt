package com.mikolove.core.network.model

import com.google.firebase.Timestamp

data class ExerciseSetNetworkEntity(
    var idExerciseSet: String,
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var order : Int,
    var createdAt: Timestamp,
    var updatedAt: Timestamp,
) {

    constructor() : this(
        "",
        0,
        0,
        0,
        0,
        0,
        Timestamp.now(),
        Timestamp.now()
    )
}