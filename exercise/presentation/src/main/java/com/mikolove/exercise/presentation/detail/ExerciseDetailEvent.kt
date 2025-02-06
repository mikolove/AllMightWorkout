package com.mikolove.exercise.presentation.detail

import com.mikolove.core.presentation.ui.UiText

sealed interface ExerciseDetailEvent{

    data class Error(val error : UiText) : ExerciseDetailEvent

    data object ExerciseSaved : ExerciseDetailEvent

}