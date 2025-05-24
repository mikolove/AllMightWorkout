package com.mikolove.core.presentation.ui.model

data class ExerciseUi(
    val idExercise : String,
    val name : String,
    //val sets : List<ExerciseSetUi>,
    val bodyPart: List<BodyPartUi>,
    val exerciseType : String,
    val isActive : Boolean,
    val createdAt : String,
    val updatedAt : String
)