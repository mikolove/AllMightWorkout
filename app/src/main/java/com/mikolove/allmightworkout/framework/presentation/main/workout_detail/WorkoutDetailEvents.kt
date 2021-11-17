package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import com.mikolove.allmightworkout.business.domain.model.Workout

sealed class WorkoutDetailEvents {

    data class AddWorkout( val workout : Workout) : WorkoutDetailEvents()
}