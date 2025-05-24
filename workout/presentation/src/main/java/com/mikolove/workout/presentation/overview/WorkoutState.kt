package com.mikolove.workout.presentation.overview

import com.mikolove.core.presentation.ui.model.GroupUi
import com.mikolove.core.presentation.ui.model.WorkoutTypeUi
import com.mikolove.core.presentation.ui.model.WorkoutUi


data class WorkoutState(
    val workoutTypes : List<WorkoutTypeUi> = emptyList(),
    val groups : List<GroupUi> = emptyList(),
    val workouts : List<WorkoutUi> = emptyList(),
    )