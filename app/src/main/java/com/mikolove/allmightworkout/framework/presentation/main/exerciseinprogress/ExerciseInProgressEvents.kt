package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class ExerciseInProgressEvents {

    data class GetExerciseById(val idExercise : String) : ExerciseInProgressEvents()

    data class UpdateExerciseSet(val set : ExerciseSet) : ExerciseInProgressEvents()

    object LoadNextSet : ExerciseInProgressEvents()

    data class UpdateActualSet(val set : ExerciseSet) : ExerciseInProgressEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : ExerciseInProgressEvents()

    data class Error(val message: GenericMessageInfo.Builder): ExerciseInProgressEvents()

    object OnRemoveHeadFromQueue: ExerciseInProgressEvents()
}