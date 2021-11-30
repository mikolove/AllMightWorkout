package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import com.mikolove.allmightworkout.business.domain.model.Exercise

data class WorkoutExercise(
    val exercise : Exercise,
    val selected : Boolean
)