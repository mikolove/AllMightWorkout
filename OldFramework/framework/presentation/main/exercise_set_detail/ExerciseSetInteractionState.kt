package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail

sealed class ExerciseSetInteractionState {

    class EditState: ExerciseSetInteractionState() {

        override fun toString(): String {
            return "EditState"
        }
    }

    class DefaultState: ExerciseSetInteractionState(){

        override fun toString(): String {
            return "DefaultState"
        }
    }
}