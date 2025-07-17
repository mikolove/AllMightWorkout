package com.mikolove.workout.presentation.overview

import com.mikolove.core.presentation.ui.UiText

sealed interface WorkoutEvent {
    data class OnCreateWorkout(val workoutId : String) : WorkoutEvent
    data class Error(val error : UiText) : WorkoutEvent
}