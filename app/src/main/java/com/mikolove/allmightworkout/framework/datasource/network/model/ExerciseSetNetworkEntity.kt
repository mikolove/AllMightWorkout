package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class ExerciseSetNetworkEntity(
    var idExerciseSet: String,
    var reps: Int,
    var weight: Int,
    var time: Int,
    var restTime: Int,
    var createdAt: Timestamp,
    var updatedAt: Timestamp,
) {
}