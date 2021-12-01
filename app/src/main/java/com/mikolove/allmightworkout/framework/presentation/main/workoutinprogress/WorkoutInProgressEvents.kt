package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class WorkoutInProgressEvents{

    data class GetWorkoutById(val idWorkout : String) : WorkoutInProgressEvents()

    object InsertHistory : WorkoutInProgressEvents()

    data class UpdateExercise(val exercise : Exercise) : WorkoutInProgressEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : WorkoutInProgressEvents()

    data class Error(val message: GenericMessageInfo.Builder): WorkoutInProgressEvents()

    object OnRemoveHeadFromQueue: WorkoutInProgressEvents()
}

/*
import com.mikolove.allmightworkout.business.domain.model.Workout
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

    class InsertHistoryEvent(
        val workout : Workout,
    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history."

        override fun eventName(): String = "InsertHistoryEvent"

        override fun shouldDisplayProgressBar(): Boolean= true
    }

    class InsertHistoryNetworkEvent(
        val workout : Workout,
    ) : WorkoutInProgressStateEvent(){
        override fun errorInfo(): String = "Error inserting history to network."

        override fun eventName(): String = "InsertHistoryNetworkEvent"

        override fun shouldDisplayProgressBar(): Boolean= true
    }


    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): WorkoutInProgressStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}*/
