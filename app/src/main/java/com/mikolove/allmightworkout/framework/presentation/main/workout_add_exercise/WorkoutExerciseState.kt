package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class WorkoutExerciseState(

    val workout : Workout? = null,

    val listWorkoutExercises : List<WorkoutExercise> = listOf(),

    //Loading
    val isLoading: Boolean? = false,

    //Search option
    var searchActive : Boolean = false,
    var query: String = "",
    var page: Int = 1,
    var isQueryExhausted: Boolean = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    )