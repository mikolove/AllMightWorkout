package com.mikolove.exercise.presentation.overview

sealed interface ExerciseAction{

    data class onUpsertExerciseClick(val exerciseId : String) : ExerciseAction

    data object onSearchClick : ExerciseAction

    data class onChipClick(val chipId : String?) : ExerciseAction
}