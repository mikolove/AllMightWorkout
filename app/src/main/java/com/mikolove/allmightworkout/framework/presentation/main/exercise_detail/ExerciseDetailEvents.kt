package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class ExerciseDetailEvents {

    data class UpdateLoadInitialValues(val load : Boolean) : ExerciseDetailEvents()

    data class UpdateExerciseName(val name : String) : ExerciseDetailEvents()

    data class UpdateExerciseIsActive(val isActive : Boolean) : ExerciseDetailEvents()

    data class UpdateExerciseBodyPart(val bodyPart : BodyPart?) : ExerciseDetailEvents()

    data class UpdateExerciseExerciseType(val exerciseType : ExerciseType) : ExerciseDetailEvents()

    data class UpdateWorkoutType(val workoutType : String) : ExerciseDetailEvents()

    object CreateExercise : ExerciseDetailEvents()

    data class GetExerciseById(val idExercise : String) : ExerciseDetailEvents()

    object GetWorkoutTypes : ExerciseDetailEvents()

    data class GetBodyParts(val idWorkoutType : String) : ExerciseDetailEvents()

    object ReloadExercise : ExerciseDetailEvents()

    object UpdateExercise : ExerciseDetailEvents()

    data class DeleteExerciseSet(val idExerciseSet : String) : ExerciseDetailEvents()

    object OnUpdateDone : ExerciseDetailEvents()

    data class OnUpdateIsPending(val isPending : Boolean) : ExerciseDetailEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : ExerciseDetailEvents()

    data class Error(val message: GenericMessageInfo.Builder): ExerciseDetailEvents()

    object OnRemoveHeadFromQueue: ExerciseDetailEvents()
}