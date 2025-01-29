package com.mikolove.exercise.presentation.detail

sealed interface ExerciseDetailAction{

    data class onBodyPartClick(val chipId : String?) :ExerciseDetailAction

    data object onUpsertClick : ExerciseDetailAction
}