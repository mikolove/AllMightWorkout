package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class ExerciseInProgressState(
    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    //eip
    val exercise: Exercise? = null,
    val actualSet : ExerciseSet? = null,
)