package com.mikolove.workout.presentation.overview

sealed interface WorkoutAction{

    data class OnWorkoutClick(val workoutId : String) : WorkoutAction

    data class OnCreateWorkoutClick(val name : String) : WorkoutAction

    data object OnSearchClick : WorkoutAction

    data class OnWorkoutTypeChipClick(val chipId : String?) : WorkoutAction

    data class OnGroupChipClick(val chipId : String?) : WorkoutAction

    data object OnAddGroupClick : WorkoutAction
}