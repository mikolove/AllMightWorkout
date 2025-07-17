package com.mikolove.workout.presentation.detail

import com.mikolove.core.presentation.ui.UiText
import com.mikolove.workout.presentation.overview.WorkoutEvent

sealed interface WorkoutDetailEvent {
    data object Test : WorkoutDetailEvent
    data class Error(val error : UiText) : WorkoutDetailEvent
}