package com.mikolove.allmightworkout.framework.presentation.main.workout_list

enum class WorkoutOrderOptions(val value: String) {
    ASC(""),
    DESC("-")
}

fun getOrderFromValue(value: String?): WorkoutOrderOptions {
    return when(value){
        WorkoutOrderOptions.ASC.value -> WorkoutOrderOptions.ASC
        WorkoutOrderOptions.DESC.value -> WorkoutOrderOptions.DESC
        else -> WorkoutOrderOptions.DESC
    }
}