package com.mikolove.workout.presentation


enum class WorkoutFilterOptions(val value: String) {
    FILTER_NAME("name"),
    FILTER_DATE_CREATED("created_at")
}

fun getFilterFromValue(value: String?): WorkoutFilterOptions {
    return when(value){
        WorkoutFilterOptions.FILTER_NAME.value -> WorkoutFilterOptions.FILTER_NAME
        WorkoutFilterOptions.FILTER_DATE_CREATED.value -> WorkoutFilterOptions.FILTER_DATE_CREATED
        else -> WorkoutFilterOptions.FILTER_NAME
    }
}