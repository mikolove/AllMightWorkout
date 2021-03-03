package com.mikolove.allmightworkout.business.domain.model

enum class ExerciseType(val type : String){
    TIME_EXERCISE("TIME_EXERCISE"),
    REP_EXERCISE("REP_EXERCISE");

    override fun toString(): String {
        return when(type){
            "TIME_EXERCISE" -> "Time"
            "REP_EXERCISE" -> "Repetition"
            else -> "Default"
        }
    }}