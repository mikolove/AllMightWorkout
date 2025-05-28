package com.mikolove.core.presentation.ui.mapper

import androidx.compose.ui.text.capitalize
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi

fun WorkoutType.toWorkoutTypeUI() : WorkoutTypeUi{
    return WorkoutTypeUi(
        idWorkoutType= this.idWorkoutType,
        name = this.name.replaceFirstChar { it.uppercase() },
        listBodyPart = this.bodyParts.map { it.toBodyPartUi() }
    )
}

fun List<WorkoutTypeUi>.toIds() : List<String>{
    return if(this.none { it.selected }) {
        this.mapIndexed{ _,workoutType ->
            workoutType.idWorkoutType
        }
    }else{
        this.filter { it.selected }.mapIndexed{ _,workoutType ->
            workoutType.idWorkoutType
        }
    }
}