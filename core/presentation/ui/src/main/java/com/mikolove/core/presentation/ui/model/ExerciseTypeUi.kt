package com.mikolove.core.presentation.ui.model

data class ExerciseTypeUi(
    val name: String,
    val selected : Boolean = false
)

fun List<ExerciseTypeUi>.getNames() : List<String>{
    return this.map { it.name }
}
