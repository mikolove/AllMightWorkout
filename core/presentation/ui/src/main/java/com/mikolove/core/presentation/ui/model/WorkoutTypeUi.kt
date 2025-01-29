package com.mikolove.core.presentation.ui.model

data class WorkoutTypeUi (
        val idWorkoutType : String,
        val name : String,
        val selected : Boolean = false,
        val listBodyPart : List<BodyPartUi> = emptyList()
)

fun List<WorkoutTypeUi>.getNames() : List<String>{
        return this.map { it.name }
}