package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.interactors.main.common.GetExerciseById
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutById

class InProgressListInteractors(
    val getWorkoutById : GetWorkoutById,
    val getExerciseById: GetExerciseById,
    val insertHistory: InsertHistory
) {
}