package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

sealed class ExerciseInteractionState {

    class EditState: ExerciseInteractionState() {

        override fun toString(): String {
            return "EditState"
        }
    }

    class DefaultState: ExerciseInteractionState(){

        override fun toString(): String {
            return "DefaultState"
        }
    }
}