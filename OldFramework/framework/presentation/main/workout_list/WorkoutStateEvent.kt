package com.mikolove.allmightworkout.framework.presentation.main.workout_list
/*

import com.mikolove.core.domain.workout.Workout
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class WorkoutStateEvent : StateEvent {

    class GetWorkoutsEvent(
    ) : WorkoutStateEvent() {

        override fun errorInfo(): String = "Error retrieving workouts."

        override fun eventName(): String = "GetWorkoutsEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetExercisesEvent() : WorkoutStateEvent() {

        override fun errorInfo(): String = "Error retrieving exercises."

        override fun eventName(): String = "GetExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }


    class GetTotalWorkoutsEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving total workouts"

        override fun eventName(): String = "GetTotalWorkouts"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetTotalExercisesEvent(

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving total exercises"

        override fun eventName(): String = "GetTotalExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class GetWorkoutByIdEvent(
        val idWorkout: String,

    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving workout by id workout."

        override fun eventName(): String = "GetWorkoutByIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class InsertWorkoutEvent(
        val name : String
    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error inserting new workout."

        override fun eventName(): String = "InsertWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class UpdateWorkoutEvent() : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error updating workout."

        override fun eventName(): String = "UpdateWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveWorkoutEvent() : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error deleting workout."

        override fun eventName(): String = "RemoveWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

    class AddExerciseToWorkoutEvent(
        val exerciseId : String,
        val workoutId : String
    ) : WorkoutStateEvent(){

        override fun errorInfo(): String = "Error adding exercise to workout."

        override fun eventName(): String = "AddExerciseToWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveExerciseFromWorkoutEvent(
        val exerciseId: String,
        val workoutId: String
    ) : WorkoutStateEvent(){

        override fun errorInfo(): String = "Error removing exercise for the specified workout."

        override fun eventName(): String = "RemoveExerciseFromWorkoutEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveMultipleWorkoutsEvent(
        val workouts : ArrayList<Workout>
    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error deleting workouts."

        override fun eventName(): String = "RemoveMultipleWorkoutsEvent"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

    class GetWorkoutTypesEvent(
    ): WorkoutStateEvent(){

        override fun errorInfo(): String = "Error getting list of workout."

        override fun eventName(): String = "GetWorkoutTypesEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartEvent(

    ): WorkoutStateEvent(){

        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartByWorkoutTypeEvent(
        val idWorkoutType: String
    ) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartByWorkoutTypeEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }
    class GetTotalBodyPartsEvent(): WorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving total bodyparts"

        override fun eventName(): String = "GetTotalBodyPartsEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetTotalBodyPartsByWorkoutTypeEvent(val idWorkoutType: String) : WorkoutStateEvent(){
        override fun errorInfo(): String = "Error retrieving total bodyparts by workoutType"

        override fun eventName(): String = "GetTotalBodyPartsByWorkoutTypeEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): WorkoutStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }

}*/
