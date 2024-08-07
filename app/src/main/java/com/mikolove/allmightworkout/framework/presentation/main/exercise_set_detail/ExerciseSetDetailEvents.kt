package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail

import com.mikolove.core.domain.state.GenericMessageInfo

sealed class ExerciseSetDetailEvents {

    data class UpdateLoadInitialValues(val load : Boolean) : ExerciseSetDetailEvents()

    data class UpdateReps(val reps : Int) : ExerciseSetDetailEvents()

    data class UpdateWeight(val weight : Int) : ExerciseSetDetailEvents()

    data class UpdateTime(val time : Int) : ExerciseSetDetailEvents()

    data class UpdateRestTime(val restTime : Int) : ExerciseSetDetailEvents()

    data class OnUpdateIsPending(val isPending : Boolean) : ExerciseSetDetailEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : ExerciseSetDetailEvents()

    data class Error(val message: GenericMessageInfo.Builder): ExerciseSetDetailEvents()

    object OnRemoveHeadFromQueue: ExerciseSetDetailEvents()
}