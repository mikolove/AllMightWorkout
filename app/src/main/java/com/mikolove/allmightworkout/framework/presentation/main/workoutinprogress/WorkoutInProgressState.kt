package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class WorkoutInProgressState(

    //wip
    val workout: Workout? = null,
    val isWorkoutDone : Boolean = false,
    val exerciseList : List<Exercise> = listOf(),

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    //eip
    /*val exercise: Exercise? = null,
    val setList : List<ExerciseSet> = listOf(),
    val actualSet : ExerciseSet? = null,*/
)