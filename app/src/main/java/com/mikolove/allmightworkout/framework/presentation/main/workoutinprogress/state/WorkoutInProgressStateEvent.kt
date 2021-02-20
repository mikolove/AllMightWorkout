package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed  class WorkoutInProgressStateEvent : StateEvent{

    class InsertHistoryExerciseEventInProgress(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise."

        override fun eventName(): String = "InsertHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryExerciseSetEventInProgress(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise set."

        override fun eventName(): String = "InsertHistoryExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryWorkoutInProgressEvent(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history workout."

        override fun eventName(): String = "InsertHistoryWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class UpdateHistoryExerciseEventInProgress(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error updating history exercise."

        override fun eventName(): String = "UpdateHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class UpdateHistoryExerciseSetEventInProgress(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error updating history exercise set."

        override fun eventName(): String = "UpdateHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class CreateInProgressStateMessageEvent(
        val stateMessage: StateMessage
    ): WorkoutInProgressStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}