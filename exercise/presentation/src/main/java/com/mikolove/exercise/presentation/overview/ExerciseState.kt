package com.mikolove.exercise.presentation.overview

import com.mikolove.core.presentation.ui.model.WorkoutTypeUi
import com.mikolove.exercise.presentation.model.ExerciseCategoryUi

data class ExerciseState(
    val workoutTypes : List<WorkoutTypeUi> = emptyList(),
    val exerciseCategories : List<ExerciseCategoryUi> = emptyList()
)