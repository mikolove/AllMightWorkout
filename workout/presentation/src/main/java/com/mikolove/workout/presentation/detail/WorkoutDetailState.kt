package com.mikolove.workout.presentation.detail

import com.mikolove.core.presentation.ui.model.WorkoutUi

data class WorkoutDetailState(
    val workoutId : String = "",
    val workoutUi : WorkoutUi = WorkoutUi(),
    val isEditMode : Boolean = false,
)