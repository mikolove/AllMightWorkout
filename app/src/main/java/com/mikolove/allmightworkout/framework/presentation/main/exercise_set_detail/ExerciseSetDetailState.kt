package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

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