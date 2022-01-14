package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class ExerciseListState(

    val listExercises: List<Exercise> = listOf(),
    val totalExercises : Int = 0,

    //Load and store filters for each list
    val list_filter: ExerciseFilterOptions = ExerciseFilterOptions.FILTER_NAME,
    val list_order: ExerciseOrderOptions = ExerciseOrderOptions.ASC,

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