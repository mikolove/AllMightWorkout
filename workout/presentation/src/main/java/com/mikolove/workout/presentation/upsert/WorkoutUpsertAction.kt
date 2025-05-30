package com.mikolove.workout.presentation.upsert

sealed interface WorkoutUpsertAction{
    data object OnBackClick : WorkoutUpsertAction
    data object OnUpsertClick : WorkoutUpsertAction
    data class OnGroupClick(val chipId : String?) : WorkoutUpsertAction
}