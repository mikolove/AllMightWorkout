package com.mikolove.allmightworkout.framework.presentation.main.workout_list

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue


data class WorkoutListState(

    //Workout relative info
    val listWorkouts: List<Workout> = listOf(),
    val totalWorkouts: Int = 0,

    //Insert workout
    val insertedWorkout : String? = null,

    //Load and store filters for each list
    val list_filter: WorkoutFilterOptions = WorkoutFilterOptions.FILTER_NAME,
    val list_order: WorkoutOrderOptions = WorkoutOrderOptions.ASC,

    //Search option
    val searchActive : Boolean = false,
    val query: String = "",
    val page: Int = 1,
    val isQueryExhausted: Boolean = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)