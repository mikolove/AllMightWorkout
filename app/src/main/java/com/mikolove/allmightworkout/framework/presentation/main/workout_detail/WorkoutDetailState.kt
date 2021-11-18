package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class WorkoutDetailState(

    val workout : Workout? = null,

    val listExercises : List<Exercise> = listOf(),
    val isUpdatePending : Boolean = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)