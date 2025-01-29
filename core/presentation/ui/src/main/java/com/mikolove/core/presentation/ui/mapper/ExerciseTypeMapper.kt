package com.mikolove.core.presentation.ui.mapper

import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.presentation.ui.model.ExerciseTypeUi

fun ExerciseType.toExerciseTypeUi() : ExerciseTypeUi {
    return ExerciseTypeUi(
        name = this.type
    )
}

fun ExerciseTypeUi.toExerciseType() : ExerciseType{
    return ExerciseType.valueOf(this.name)
}