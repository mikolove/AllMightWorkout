package com.mikolove.allmightworkout.framework.presentation.main.workout_list

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue


data class WorkoutListState(

    //Workout relative info
    var listWorkouts: List<Workout> = listOf(),
    var totalWorkouts: Int = 0,

    //Insert workout
    var insertedWorkout : String? = null,

    //Load and store filters for each list
    var list_filter: WorkoutFilterOptions = WorkoutFilterOptions.FILTER_NAME,
    var list_order: WorkoutOrderOptions = WorkoutOrderOptions.ASC,

    //Search option
    var searchActive : Boolean = false,
    var query: String = "",
    var page: Int = 1,
    var isQueryExhausted: Boolean = false,

    //Loading
    val isLoading: Boolean? = false,

    //Queue
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
)