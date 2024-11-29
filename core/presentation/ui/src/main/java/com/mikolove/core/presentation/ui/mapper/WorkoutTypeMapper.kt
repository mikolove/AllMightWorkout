package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi

fun WorkoutType.toWorkoutTypeUI() : WorkoutTypeUi{
    return WorkoutTypeUi(
        idWorkoutType= this.idWorkoutType,
        name = this.name,
        listBodyPart = this.bodyParts.map { it.toBodyPartUi() }
    )
}