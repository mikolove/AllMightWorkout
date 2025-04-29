package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class WorkoutExerciseState(

    val workout : Workout? = null,

    val listWorkoutExercises : List<WorkoutExercise> = listOf(),

    //Loading
    val isLoading: Boolean? = false,
    val isUpdateDone : Boolean = false,

    //Search option
    val searchActive : Boolean = false,
    val query: String = "",
    val page: Int = 1,
    val isQueryExhausted: Boolean = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    )