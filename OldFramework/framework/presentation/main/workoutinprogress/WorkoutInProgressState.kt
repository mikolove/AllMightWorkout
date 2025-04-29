package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class WorkoutInProgressState(

    //wip
    val workout: Workout? = null,
    val isWorkoutDone : Boolean = false,
    val exerciseList : List<Exercise> = listOf(),

    //close view
    val exitWorkout : Boolean? = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    //eip
    /*val exercise: Exercise? = null,
    val setList : List<ExerciseSet> = listOf(),
    val actualSet : ExerciseSet? = null,*/
)