package com.mikolove.allmightworkout.framework.datasource.network.model

import com.google.firebase.Timestamp

data class ExerciseNetworkEntity(
    var idExercise: String,
    var name: String,
    var sets: List<ExerciseSetNetworkEntity>,
    var bodyPart: BodyPartNetworkEntity?,
    var exerciseType: String,
    var isActive: Boolean,
    var createdAt: Timestamp,
    var updatedAt: Timestamp
) {
}