package com.mikolove.exercise.presentation.overview

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi
import com.mikolove.exercise.presentation.model.ExerciseUi

data class ExerciseState(
    val workoutTypes : List<WorkoutTypeUi> = emptyList(),
    val exercises : List<ExerciseUi> = emptyList(),
)