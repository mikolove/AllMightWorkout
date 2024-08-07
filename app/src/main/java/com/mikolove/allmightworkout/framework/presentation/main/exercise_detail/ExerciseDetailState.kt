package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class ExerciseDetailState(

    val idExercise : String = "",
    val exercise : Exercise? = null,
    val isInCache : Boolean = false,

    //Init values
    val loadInitialValues : Boolean = false,

    //Update
    val isUpdatePending : Boolean = false,
    val isUpdateDone : Boolean = false,

    //List
    val workoutTypes : List<WorkoutType> = listOf(),
    val exerciseTypes : List<ExerciseType> = listOf(),
    val bodyParts : List<BodyPart> = listOf(),

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)