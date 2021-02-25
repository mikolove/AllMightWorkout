package com.mikolove.allmightworkout.framework.presentation.main.exercise.state

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