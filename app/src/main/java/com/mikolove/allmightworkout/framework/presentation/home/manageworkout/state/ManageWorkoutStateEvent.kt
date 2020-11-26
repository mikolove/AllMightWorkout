package com.mikolove.allmightworkout.framework.presentation.home.manageworkout.state

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class ManageWorkoutStateEvent : StateEvent{

    class InsertWorkout(
        name: String
    ) : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting new workout."

        override fun eventName(): String = "InsertWorkout"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateWorkout(
        workout : Workout
    ) : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error updating new workout."

        override fun eventName(): String = "UpdateWorkout"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveWorkouts(
        listWorkouts : ArrayList<Workout>
    ) : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error deleting workouts."

        override fun eventName(): String = "RemoveWorkouts"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

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