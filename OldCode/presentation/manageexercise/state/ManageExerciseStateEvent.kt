package com.mikolove.allmightworkout.oldCode.presentation.manageexercise.state

/*
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.oldCode.presentation.manageworkout.state.ManageWorkoutStateEvent

sealed class ManageExerciseStateEvent : StateEvent{

    class InsertExerciseEvent(
        name : String
    ) : ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error inserting new exercise."

        override fun eventName(): String = "InsertExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class UpdateExerciseEvent(): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error updating exercise."

        override fun eventName(): String = "UpdateExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveExerciseEvent(exercise : Exercise): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error deleting exercise."

        override fun eventName(): String = "RemoveExerciseByIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertExerciseSetEvent(exerciseSetId : String): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error adding exercise set."

        override fun eventName(): String = "InsertExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class UpdateExerciseSetEvent(): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error updating exercise set."

        override fun eventName(): String = "UpdateExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class RemoveExerciseSetEvent(exerciseId : String) : ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error removing exercise set."

        override fun eventName(): String = "RemoveExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

}*/
