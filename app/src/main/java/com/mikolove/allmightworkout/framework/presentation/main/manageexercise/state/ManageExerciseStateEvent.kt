package com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.StateEvent

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

    class RemoveExerciseByIdEvent(): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error deleting exercise."

        override fun eventName(): String = "RemoveExerciseByIdEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertExerciseSetEvent(exerciseId : String): ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error adding exercise set."

        override fun eventName(): String = "InsertExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveExerciseSetEvent(exerciseId : String) : ManageExerciseStateEvent(){
        override fun errorInfo(): String = "Error removing exercise set."

        override fun eventName(): String = "RemoveExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

}