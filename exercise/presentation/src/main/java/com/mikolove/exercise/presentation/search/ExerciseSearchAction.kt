package com.mikolove.exercise.presentation.search

sealed interface ExerciseSearchAction{

    data object OnBackClick :ExerciseSearchAction
    data class OnDetailClick(val exerciseId: String) : ExerciseSearchAction
}