package com.mikolove.exercise.presentation.overview

sealed interface ExerciseAction{

    data class onExerciseClick(val exerciseId : String) : ExerciseAction

    data object onAddExerciseClick : ExerciseAction

    data object onSearchClick : ExerciseAction

    data class onChipClick(val chipId : String?) : ExerciseAction
}