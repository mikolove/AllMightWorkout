package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

sealed class ChronometerState {

    class IdleState : ChronometerState() {
        override fun toString(): String {
            return "IdleState"
        }
    }

    class RunningState : ChronometerState() {
        override fun toString(): String {
            return "RunningState"
        }
    }

    class StopState : ChronometerState() {
        override fun toString(): String {
            return "StopState"
        }
    }

    class RestTimeState : ChronometerState() {
        override fun toString(): String {
            return "RestTimeState"
        }
    }

    class SaveState : ChronometerState(){
        override fun toString(): String {
            return "SaveState"
        }
    }

    class CloseState : ChronometerState(){
        override fun toString(): String {
            return "CloseState"
        }
    }

}