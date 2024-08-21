package com.mikolove.workout.presentation

import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue


data class WorkoutListState(

    //Workout relative info
    //val listWorkouts: List<Workout> = listOf(),
    //val totalWorkouts: Int = 0,

    //Insert workout
    //val insertedWorkout : String? = null,

    //Load and store filters for each list
    val list_filter: WorkoutFilterOptions = WorkoutFilterOptions.FILTER_NAME,
    val list_order: WorkoutOrderOptions = WorkoutOrderOptions.ASC,

    //Search option
    //val searchActive : Boolean = false,
    //val query: String = "",
    //val page: Int = 1,
    //val isQueryExhausted: Boolean = false,

    //Loading
    val isLoading: Boolean? = false,

    //WorkoutType filter chip
    val listWorkoutTypeFilter : List<WorkoutTypeFilter> = listOf(),

    //WorkoutCollection By Group
    val listWorkoutCollection : List<WorkoutCollection> = listOf(),

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)