package com.mikolove.core.presentation.ui.model

data class ExerciseCategoryUi(
    val idWorkoutType : String,
    val name : String,
    val exercises : List<ExerciseUi>
)