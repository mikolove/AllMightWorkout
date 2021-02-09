package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState

class ManageWorkoutListInteractors(
    val addExerciseToWorkout: AddExerciseToWorkout,
    val insertWorkout: InsertWorkout,
    val removeExerciseFromWorkout: RemoveExerciseFromWorkout,
    val removeWorkout: RemoveWorkout<ManageWorkoutViewState>,
    val updateWorkout: UpdateWorkout
) {
}