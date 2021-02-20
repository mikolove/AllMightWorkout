package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState


class ManageExerciseListInteractors(
    val insertExercise: InsertExercise,
    val insertExerciseSet: InsertExerciseSet,
    val removeExercise: RemoveExercise<ExerciseViewState>,
    val removeExerciseSet: RemoveExerciseSet,
    val updateExercise: UpdateExercise,
    val updateExerciseSet: UpdateExerciseSet
) {
}