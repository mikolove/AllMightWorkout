package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

sealed class ChronometerButtonState {

    class StartButtonState : ChronometerButtonState(){
        override fun toString(): String {
            return "StartButtonState"
        }
    }

    class StopButtonState : ChronometerButtonState(){
        override fun toString(): String {
            return "StopButtonState"
        }
    }

    class RestButtonState : ChronometerButtonState(){
        override fun toString(): String {
            return "RestButtonState"
        }
    }

    class ActiveButtonState : ChronometerButtonState(){
        override fun toString(): String {
            return "ActiveButtonState"
        }
    }

    class PassiveButtonState : ChronometerButtonState(){
        override fun toString(): String {
            return "PassiveButtonState"
        }
    }
}