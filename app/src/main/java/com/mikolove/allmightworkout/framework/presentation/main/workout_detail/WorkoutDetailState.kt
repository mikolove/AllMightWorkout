package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class WorkoutDetailState(

    val workout : Workout? = null,

    val listExercises : List<Exercise> = listOf(),
    val isUpdatePending : Boolean = false,
    val isUpdateDone : Boolean  = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)