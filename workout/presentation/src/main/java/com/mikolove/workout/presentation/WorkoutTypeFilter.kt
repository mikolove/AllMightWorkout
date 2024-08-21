package com.mikolove.workout.presentation

import com.mikolove.core.domain.workouttype.WorkoutType

data class WorkoutTypeFilter(
    val workoutTypeId : String,
    val workoutType : WorkoutType,
    val selected : Boolean = false,
)