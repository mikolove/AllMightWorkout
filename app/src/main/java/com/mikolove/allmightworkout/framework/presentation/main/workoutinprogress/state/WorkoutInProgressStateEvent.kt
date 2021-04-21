package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed  class WorkoutInProgressStateEvent : StateEvent{

    class GetWorkoutByIdEvent(
        val idWorkout : String
    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error retrieving workout by id."

        override fun eventName(): String = "GetWorkoutByIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertHistoryExerciseEvent(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise."

        override fun eventName(): String = "InsertHistoryExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryExerciseSetEvent(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history exercise set."

        override fun eventName(): String = "InsertHistoryExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }

    class InsertHistoryWorkoutEvent(

    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history workout."

        override fun eventName(): String = "InsertHistoryWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean= false
    }


    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): WorkoutInProgressStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}