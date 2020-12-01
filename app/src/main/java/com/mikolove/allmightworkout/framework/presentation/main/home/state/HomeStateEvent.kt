package com.mikolove.allmightworkout.framework.presentation.main.home.state

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class HomeStateEvent : StateEvent{

    class GetWorkoutsEvent(
        val clearLayoutManagerState: Boolean = true
    ) : HomeStateEvent() {

        override fun errorInfo(): String = "Error retrieving workouts."

        override fun eventName(): String = "GetWorkoutsEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetTotalWorkoutsEvent(
        val clearLayoutManagerState: Boolean = true
    ) : HomeStateEvent(){
        override fun errorInfo(): String = "Error retrieving total workouts"

        override fun eventName(): String = "GetTotalWorkouts"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetWorkoutTypesEvent(
        val clearLayoutManagerState: Boolean = true
    ): HomeStateEvent(){

        override fun errorInfo(): String = "Error getting list of workout."

        override fun eventName(): String = "GetWorkoutTypesEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartEvent(
        val clearLayoutManagerState: Boolean = true
    ): HomeStateEvent(){

        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetExercisesEvent(
        val clearLayoutManagerState: Boolean = true
    ) : HomeStateEvent() {

        override fun errorInfo(): String = "Error retrieving exercises."

        override fun eventName(): String = "GetExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }


    class GetTotalExercisesEvent(
        val clearLayoutManagerState: Boolean = true
    ) : HomeStateEvent(){
        override fun errorInfo(): String = "Error retrieving total exercises"

        override fun eventName(): String = "GetTotalExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetWorkoutByIdEvent(
        val clearLayoutManagerState: Boolean = true
    ) : HomeStateEvent(){
        override fun errorInfo(): String = "Error retrieving workout by id workout."

        override fun eventName(): String = "GetWorkoutByIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): HomeStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}