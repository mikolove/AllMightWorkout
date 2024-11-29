package com.mikolove.core.presentation.ui.model

data class WorkoutTypeUi (
        val idWorkoutType : String,
        val name : String,
        val enabled : Boolean = false,
        val listBodyPart : List<BodyPartUi> = emptyList()
)