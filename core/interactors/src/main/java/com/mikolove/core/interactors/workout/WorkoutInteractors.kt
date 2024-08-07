package com.mikolove.core.interactors.workout

import com.mikolove.allmightworkout.business.interactors.main.common.*

class WorkoutInteractors(
    val getWorkouts: GetWorkouts,
    val getExercises: GetExercises,
    val getWorkoutById: GetWorkoutById,
    val getTotalWorkouts: GetTotalWorkouts,
    val getTotalExercises: GetTotalExercises,
    val insertWorkout: InsertWorkout,
    val updateWorkout: UpdateWorkout,
    val removeWorkout: RemoveWorkout,
    val removeMultipleWorkouts: RemoveMultipleWorkouts,
    val addExerciseToWorkout: AddExerciseToWorkout,
    val removeExerciseFromWorkout: RemoveExerciseFromWorkout,
    val getWorkoutTypes: GetWorkoutTypes,
    val getBodyParts: GetBodyParts,
    val getTotalBodyParts: GetTotalBodyParts,
    val getTotalBodyPartsByWorkoutType: GetTotalBodyPartsByWorkoutType,
    val getWorkoutOrderAndFilter : GetWorkoutOrderAndFilter
)
{
}