package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import com.mikolove.allmightworkout.business.domain.model.WorkoutType

data class WorkoutTypeFilter(
    val workoutTypeId : String,
    val workoutType : WorkoutType,
    val selected : Boolean = false,
)