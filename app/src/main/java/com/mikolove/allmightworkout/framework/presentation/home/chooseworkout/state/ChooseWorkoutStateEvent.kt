package com.mikolove.allmightworkout.framework.presentation.home.chooseworkout.state

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class ChooseWorkoutStateEvent : StateEvent{

    class GetWorkoutsEvent(
        val clearLayoutManagerState: Boolean = true
    ) : ChooseWorkoutStateEvent() {

        override fun errorInfo(): String = "Error retrieving workouts."

        override fun eventName(): String = "GetWorkoutsEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetTotalWorkoutsEvent(
        val clearLayoutManagerState: Boolean = true
    ) : ChooseWorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving total workouts"

        override fun eventName(): String = "GetTotalWorkouts"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetWorkoutTypesEvent(
        val clearLayoutManagerState: Boolean = true
    ): ChooseWorkoutStateEvent(){

        override fun errorInfo(): String = "Error getting list of workout."

        override fun eventName(): String = "GetWorkoutTypesEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartEvent(
        val clearLayoutManagerState: Boolean = true
    ): ChooseWorkoutStateEvent(){

        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): ChooseWorkoutStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }

}