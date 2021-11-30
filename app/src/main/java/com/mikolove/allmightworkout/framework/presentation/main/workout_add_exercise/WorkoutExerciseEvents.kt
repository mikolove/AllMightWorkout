package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo

sealed class WorkoutExerciseEvents {

    object NewSearch : WorkoutExerciseEvents()

    object ReloadList : WorkoutExerciseEvents()

    object NextPage : WorkoutExerciseEvents()

    data class UpdateQuery(val query : String) : WorkoutExerciseEvents()

    data class GetWorkoutById(val idWorkout : String) : WorkoutExerciseEvents()

    data class AddExerciseToWorkout(val idExercise: String) : WorkoutExerciseEvents()

    data class RemoveExerciseFromWorkout(val idExercise: String) : WorkoutExerciseEvents()

    class LaunchDialog(val message : GenericMessageInfo.Builder) : WorkoutExerciseEvents()

    object OnRemoveHeadFromQueue: WorkoutExerciseEvents()
}