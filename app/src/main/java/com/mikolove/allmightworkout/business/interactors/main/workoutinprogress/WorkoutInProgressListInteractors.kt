package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutById

class WorkoutInProgressListInteractors(
    val getWorkoutById : GetWorkoutById,
    val insertHistory: InsertHistory,
    val insertHistoryWorkout: InsertHistoryWorkout,
    val insertHistoryExercise: InsertHistoryExercise,
    val insertHistoryExerciseSet: InsertHistoryExerciseSet
) {
}