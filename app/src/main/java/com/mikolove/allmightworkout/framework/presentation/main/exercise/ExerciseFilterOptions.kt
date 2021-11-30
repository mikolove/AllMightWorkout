package com.mikolove.allmightworkout.framework.presentation.main.exercise

enum class ExerciseFilterOptions(val value: String) {
    FILTER_NAME("name"),
    FILTER_DATE_CREATED("created_at")
}

fun getFilterFromValue(value: String?): ExerciseFilterOptions {
    return when(value){
        ExerciseFilterOptions.FILTER_NAME.value -> ExerciseFilterOptions.FILTER_NAME
        ExerciseFilterOptions.FILTER_DATE_CREATED.value -> ExerciseFilterOptions.FILTER_DATE_CREATED
        else -> ExerciseFilterOptions.FILTER_NAME
    }
}