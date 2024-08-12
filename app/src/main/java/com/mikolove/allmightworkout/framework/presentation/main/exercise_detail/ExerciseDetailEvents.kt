package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.state.GenericMessageInfo

sealed class ExerciseDetailEvents {

    data class UpdateLoadInitialValues(val load : Boolean) : ExerciseDetailEvents()

    object AddSet :ExerciseDetailEvents()

    data class RemoveSet(val set : ExerciseSet) : ExerciseDetailEvents()

    data class UpdateSet(val set : ExerciseSet) : ExerciseDetailEvents()

    object InsertExercise : ExerciseDetailEvents()

    data class UpdateIsInCache(val isInCache : Boolean) : ExerciseDetailEvents()

    data class UpdateExerciseName(val name : String) : ExerciseDetailEvents()

    data class UpdateExerciseIsActive(val isActive : Boolean) : ExerciseDetailEvents()

    data class UpdateExerciseBodyPart(val bodyPart : BodyPart?) : ExerciseDetailEvents()

    data class UpdateExerciseExerciseType(val exerciseType : ExerciseType) : ExerciseDetailEvents()

    object CreateExercise : ExerciseDetailEvents()

    data class GetExerciseById(val idExercise : String) : ExerciseDetailEvents()

    object GetWorkoutTypes : ExerciseDetailEvents()

    object GetExerciseTypes : ExerciseDetailEvents()

    data class GetBodyParts(val idWorkoutType : String) : ExerciseDetailEvents()

    object UpdateExercise : ExerciseDetailEvents()

    data class OnUpdateIsPending(val isPending : Boolean) : ExerciseDetailEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : ExerciseDetailEvents()

    data class Error(val message: GenericMessageInfo.Builder): ExerciseDetailEvents()

    object OnRemoveHeadFromQueue: ExerciseDetailEvents()
}