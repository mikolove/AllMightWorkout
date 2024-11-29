package com.mikolove.exercise.presentation.model

data class ExerciseCategoryUi(
    val idWorkoutType : String,
    val name : String,
    val exercises : List<ExerciseUi>
)