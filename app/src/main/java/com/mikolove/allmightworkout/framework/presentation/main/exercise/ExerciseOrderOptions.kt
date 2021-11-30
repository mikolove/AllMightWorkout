package com.mikolove.allmightworkout.framework.presentation.main.exercise

enum class ExerciseOrderOptions(val value: String) {
    ASC(""),
    DESC("-")
}

fun getOrderFromValue(value: String?): ExerciseOrderOptions {
    return when(value){
        ExerciseOrderOptions.ASC.value -> ExerciseOrderOptions.ASC
        ExerciseOrderOptions.DESC.value -> ExerciseOrderOptions.DESC
        else -> ExerciseOrderOptions.DESC
    }
}