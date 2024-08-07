package com.mikolove.core.domain.exercise

enum class ExerciseType(val type : String){
    TIME_EXERCISE("Time"),
    REP_EXERCISE("Repetition");

    override fun toString(): String {
        return type
    }
}
