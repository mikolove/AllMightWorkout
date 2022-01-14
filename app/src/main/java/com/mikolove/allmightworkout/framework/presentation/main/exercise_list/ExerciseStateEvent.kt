package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

/*
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.domain.state.StateMessage

sealed class ExerciseStateEvent : StateEvent{

    class GetExercisesEvent(
        val clearLayoutManagerState: Boolean = true
    ) : ExerciseStateEvent() {

        override fun errorInfo(): String = "Error retrieving exercises."

        override fun eventName(): String = "GetExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class GetExerciseByIdEvent(
        val idExercise : String
    ) : ExerciseStateEvent(){

        override fun errorInfo(): String = "Error retrieving exercise by id."

        override fun eventName(): String = "    class GetExerciseByIdEvent(\n"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetExerciseSetByIdExerciseEvent(
        val idExercise: String
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error retrieving sets by id exercise"

        override fun eventName(): String = "GetExerciseSetByIdExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetTotalExercisesEvent(
        val clearLayoutManagerState: Boolean = true
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error retrieving total exercises"

        override fun eventName(): String = "GetTotalExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertExerciseEvent(
        val name : String,
        val exerciseType: ExerciseType,
        val bodyPart: BodyPart
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error inserting new exercise."

        override fun eventName(): String = "InsertExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = true
    }

    class UpdateExerciseEvent(): ExerciseStateEvent(){
        override fun errorInfo(): String = "Error updating exercise."

        override fun eventName(): String = "UpdateExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveExerciseEvent(): ExerciseStateEvent(){
        override fun errorInfo(): String = "Error deleting exercise."

        override fun eventName(): String = "RemoveExerciseEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveMultipleExercisesEvent(
        val exercises : ArrayList<Exercise>
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error deleting exercises."

        override fun eventName(): String = "RemoveMultipleExercisesEvent"

        override fun shouldDisplayProgressBar(): Boolean = false

    }

    class InsertExerciseSetEvent(
        val exerciseSet : ExerciseSet,
        val idExercise : String ): ExerciseStateEvent(){
        override fun errorInfo(): String = "Error adding exercise set."

        override fun eventName(): String = "InsertExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class InsertMultipleExerciseSetEvent(
        val sets : ArrayList<ExerciseSet>,
        val idExercise: String
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error inserting some new exercise sets."

        override fun eventName(): String = "InsertMultipleExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateMultipleExerciseSetEvent(
        val sets : ArrayList<ExerciseSet>,
        val idExercise: String
    ) : ExerciseStateEvent(){

        override fun errorInfo(): String = "Error updating some exercise sets."

        override fun eventName(): String = "UpdateMultipleExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateNetworkExerciseSetsEvent(
        val sets : ArrayList<ExerciseSet>,
        val deletedSets : ArrayList<ExerciseSet>,
        val idExercise: String
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error updating network exercises sets"

        override fun eventName(): String ="UpdateNetworkExerciseSetsEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class RemoveMultipleExerciseSetEvent(
        val sets : ArrayList<ExerciseSet>,
        val idExercise: String
    ) : ExerciseStateEvent(){

        override fun errorInfo(): String = "Error removing some exercise sets."

        override fun eventName(): String = "RemoveMultipleExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class UpdateExerciseSetEvent(): ExerciseStateEvent(){
        override fun errorInfo(): String = "Error updating exercise set."

        override fun eventName(): String = "UpdateExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }


    class RemoveExerciseSetEvent(exerciseId : String) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error removing exercise set."

        override fun eventName(): String = "RemoveExerciseSetEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetWorkoutTypesEvent(
        val clearLayoutManagerState: Boolean = true
    ): ExerciseStateEvent(){

        override fun errorInfo(): String = "Error getting list of workout."

        override fun eventName(): String = "GetWorkoutTypesEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartEvent(
        val clearLayoutManagerState: Boolean = true
    ): ExerciseStateEvent(){

        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartEvent"

        override fun shouldDisplayProgressBar() : Boolean = true
    }

    class GetBodyPartByWorkoutTypeEvent(
        val idWorkoutType: String
    ) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error getting list of bodypart."

        override fun eventName(): String = "GetBodyPartByWorkoutTypeEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetTotalBodyPartsEvent(): ExerciseStateEvent(){
        override fun errorInfo(): String = "Error retrieving total bodyparts"

        override fun eventName(): String = "GetTotalBodyPartsEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class GetTotalBodyPartsByWorkoutTypeEvent(val idWorkoutType: String) : ExerciseStateEvent(){
        override fun errorInfo(): String = "Error retrieving total bodyparts by workoutType"

        override fun eventName(): String = "GetTotalBodyPartsByWorkoutTypeEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): ExerciseStateEvent(){

        override fun errorInfo(): String = "Error creating a new state message."

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar() : Boolean = false
    }
}*/
