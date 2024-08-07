package com.mikolove.allmightworkout.framework.presentation.main.compose.ui

import com.mikolove.core.domain.workout.Workout

data class WorkoutCollection(
    val id : String,
    val name : String,
    val workouts : List<Workout>
)