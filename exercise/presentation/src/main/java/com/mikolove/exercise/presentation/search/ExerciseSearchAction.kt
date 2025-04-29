package com.mikolove.exercise.presentation.search

sealed interface ExerciseSearchAction{

    data object onBackClick :ExerciseSearchAction

    data class onDetailClick(val exerciseId: String) : ExerciseSearchAction
}