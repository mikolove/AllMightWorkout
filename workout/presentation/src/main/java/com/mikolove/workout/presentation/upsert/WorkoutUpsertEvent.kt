package com.mikolove.workout.presentation.upsert

import com.mikolove.core.presentation.ui.UiText

sealed interface WorkoutUpsertEvent{
    data class OnFinish(val workoutId : String) : WorkoutUpsertEvent
    data class Error(val error : UiText) : WorkoutUpsertEvent
}