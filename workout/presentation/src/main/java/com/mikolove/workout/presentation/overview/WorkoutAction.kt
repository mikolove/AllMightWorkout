package com.mikolove.workout.presentation.overview

sealed interface WorkoutAction{

    data class onUpsertWorkoutClick(val workoutId : String) : WorkoutAction

    data object onSearchClick : WorkoutAction

    data class onWorkoutTypeChipClick(val chipId : String?) : WorkoutAction

    data class onGroupChipClick(val chipId : String?) : WorkoutAction

    data object onAddGroupClick : WorkoutAction
}