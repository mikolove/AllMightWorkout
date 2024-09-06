package com.mikolove.core.domain.exercise

enum class ExerciseType(val type : String){
    TIME_EXERCISE("Time"),
    REP_EXERCISE("Repetition"),
    OTHER("Other");

    companion object {
        fun from(findValue: String?): ExerciseType = entries.find { it.type == (findValue ?: "") } ?: OTHER
    }

    override fun toString(): String {
        return type
    }
}
