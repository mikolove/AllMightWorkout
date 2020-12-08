package com.mikolove.allmightworkout.framework.presentation.main.workout.state

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed  class WorkoutStateEvent : StateEvent{

    class InsertHistoryExerciseEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise."

        override fun eventName(): String = "InsertHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryExerciseSetEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise set."

        override fun eventName(): String = "InsertHistoryExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryWorkoutEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting history workout."

        override fun eventName(): String = "InsertHistoryWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class UpdateHistoryExerciseEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error updating history exercise."

        override fun eventName(): String = "UpdateHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class UpdateHistoryExerciseSetEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error updating history exercise set."

        override fun eventName(): String = "UpdateHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): WorkoutStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}