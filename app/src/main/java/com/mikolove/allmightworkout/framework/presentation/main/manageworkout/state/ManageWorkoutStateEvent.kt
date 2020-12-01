package com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state

import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class ManageWorkoutStateEvent : StateEvent{

    class InsertWorkoutEvent(
        name: String
    ) : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting new workout."

        override fun eventName(): String = "InsertWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateWorkoutEvent() : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error updating workout."

        override fun eventName(): String = "UpdateWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveWorkoutsEvent(
        listWorkouts : ArrayList<Workout>
    ) : ManageWorkoutStateEvent(){
        override fun errorInfo(): String = "Error deleting workouts."

        override fun eventName(): String = "RemoveWorkoutsEvent"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

    class AddExerciseToWorkoutEvent(
        exerciseId : String
    ) : ManageWorkoutStateEvent(){

        override fun errorInfo(): String = "Error adding exercise to workout."

        override fun eventName(): String = "AddExerciseToWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveExerciseFromWorkoutEvent(
        exerciseId: String
    ) : ManageWorkoutStateEvent(){

        override fun errorInfo(): String = "Error removing exercise for the specified workout."

        override fun eventName(): String = "RemoveExerciseFromWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetExercisesFromWorkoutIdEvent() : ManageWorkoutStateEvent(){

        override fun errorInfo(): String = "Error retrieving list of exercise with the specified workoutId."

        override fun eventName(): String = "GetExercisesFromWorkoutIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetWorkoutsFromExerciseIdEvent(
        exerciseId: String
    ) : ManageWorkoutStateEvent(){

        override fun errorInfo(): String = "Error retrieving list of workouts with the specified exerciseId."

        override fun eventName(): String = "GetWorkoutsFromExerciseIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
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