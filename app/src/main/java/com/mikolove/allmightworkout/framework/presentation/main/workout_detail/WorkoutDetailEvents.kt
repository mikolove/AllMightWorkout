package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class WorkoutDetailEvents {

    data class GetWorkoutById(val idWorkout : String) : WorkoutDetailEvents()

    object ReloadWorkout : WorkoutDetailEvents()

    object UpdateWorkout : WorkoutDetailEvents()

    object RemoveWorkout : WorkoutDetailEvents()

    data class DeleteExercise(val idExercise : String) : WorkoutDetailEvents()

    data class OnUpdateWorkout(val name : String, val isActive :Boolean) : WorkoutDetailEvents()

    object OnUpdateDone : WorkoutDetailEvents()

    data class OnUpdateIsPending(val isPending : Boolean) : WorkoutDetailEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : WorkoutDetailEvents()

    data class Error(val message: GenericMessageInfo.Builder): WorkoutDetailEvents()

    object OnRemoveHeadFromQueue: WorkoutDetailEvents()
}