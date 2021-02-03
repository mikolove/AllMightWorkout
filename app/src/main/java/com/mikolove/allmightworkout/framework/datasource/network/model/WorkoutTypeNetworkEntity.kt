package com.mikolove.allmightworkout.framework.datasource.network.model

import com.mikolove.allmightworkout.business.domain.model.BodyPart

data class WorkoutTypeNetworkEntity(
    var idWorkoutType : String,
    var name : String,
    var bodyParts : List<BodyPartNetworkEntity>?
) {
}