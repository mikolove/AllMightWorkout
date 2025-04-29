package com.mikolove.exercise.presentation.detail

sealed interface ExerciseDetailAction{

    data object onBackClick : ExerciseDetailAction

    data class onBodyPartClick(val chipId : String?) :ExerciseDetailAction

    data object onUpsertClick : ExerciseDetailAction

    data class onDeleteClick(val exerciseId : String) : ExerciseDetailAction
}