package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import com.mikolove.core.domain.exercise.Exercise

data class WorkoutExercise(
    val exercise : Exercise,
    val selected : Boolean
)