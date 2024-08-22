package com.mikolove.allmightworkout.framework.presentation.main.workout_detail


sealed class WorkoutInteractionState {

    class EditState: WorkoutInteractionState() {

        override fun toString(): String {
            return "EditState"
        }
    }

    class DefaultState: WorkoutInteractionState(){

        override fun toString(): String {
            return "DefaultState"
        }
    }

}