package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class WorkoutNetworkEntity (
    var idWorkout: String,
    var name: String,
    var exerciseIds : List<String>,
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp){

    constructor() : this(
        "",
        "",
        listOf(),
        true,
        Timestamp.now(),
        Timestamp.now()
    )

}