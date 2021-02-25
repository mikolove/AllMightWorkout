package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState

class ExerciseInteractors(
    val getExercises: GetExercises,
    val getExerciseById : GetExerciseById,
    val getTotalExercises: GetTotalExercises,
    val insertExercise: InsertExercise,
    val insertExerciseSet: InsertExerciseSet,
    val updateExercise: UpdateExercise,
    val updateExerciseSet: UpdateExerciseSet,
    val removeExercise: RemoveExercise<ExerciseViewState>,
    val removeMultipleExercises: RemoveMultipleExercises,
    val removeExerciseSet: RemoveExerciseSet,
    val getWorkoutTypes: GetWorkoutTypes,
    val getBodyParts: GetBodyParts,
    val getTotalBodyParts: GetTotalBodyParts,
    val getTotalBodyPartsByWorkoutType: GetTotalBodyPartsByWorkoutType
) {
}