package com.mikolove.workout.presentation.detail

import com.mikolove.core.presentation.ui.model.WorkoutUi

data class WorkoutDetailState(
    val workoutId : String = "",
    val isEditMode : Boolean = false,
)