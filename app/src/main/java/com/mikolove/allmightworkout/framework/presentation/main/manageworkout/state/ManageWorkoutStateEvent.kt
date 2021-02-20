package com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent

sealed class ManageWorkoutStateEvent : StateEvent{


    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): ManageWorkoutStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}