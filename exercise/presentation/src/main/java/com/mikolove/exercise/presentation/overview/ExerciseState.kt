package com.mikolove.exercise.presentation.overview

import com.mikolove.core.presentation.ui.model.WorkoutTypeUi
import com.mikolove.core.presentation.ui.model.ExerciseUi

data class ExerciseState(
    val workoutTypes : List<WorkoutTypeUi> = emptyList(),
    val exercises : List<ExerciseUi> = emptyList(),
)