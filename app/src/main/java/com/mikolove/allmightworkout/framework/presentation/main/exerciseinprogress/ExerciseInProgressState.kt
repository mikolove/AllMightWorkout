package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class ExerciseInProgressState(
    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    //eip
    val exercise: Exercise? = null,
    val actualSet : ExerciseSet? = null,
)