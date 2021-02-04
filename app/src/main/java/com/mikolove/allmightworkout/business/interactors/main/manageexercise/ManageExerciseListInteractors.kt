package com.mikolove.allmightworkout.business.interactors.main.manageexercise

import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseViewState

class ManageExerciseListInteractors(
    val insertExercise: InsertExercise,
    val insertExerciseSet: InsertExerciseSet,
    val removeExercise: RemoveExercise<ManageExerciseViewState>,
    val removeExerciseSet: RemoveExerciseSet,
    val updateExercise: UpdateExercise,
    val updateExerciseSet: UpdateExerciseSet
) {
}