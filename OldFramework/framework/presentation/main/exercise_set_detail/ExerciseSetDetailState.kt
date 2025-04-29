package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail

import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class ExerciseSetDetailState(

    //ExerciseSet
    val set : ExerciseSet? = null,
    val exerciseType : ExerciseType? = null,

    //Init values
    val loadInitialValues : Boolean = false,

    //Update
    val isUpdatePending : Boolean = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)