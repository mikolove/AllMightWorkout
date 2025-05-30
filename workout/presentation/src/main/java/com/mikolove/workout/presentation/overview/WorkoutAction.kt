package com.mikolove.workout.presentation.overview

sealed interface WorkoutAction{

    data class OnUpsertWorkoutClick(val workoutId : String) : WorkoutAction

    data object OnSearchClick : WorkoutAction

    data class OnWorkoutTypeChipClick(val chipId : String?) : WorkoutAction

    data class OnGroupChipClick(val chipId : String?) : WorkoutAction

    data object OnAddGroupClick : WorkoutAction
}