package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.interactors.main.common.*

class HomeListInteractors(
    val getBodyParts: GetBodyParts,
    val getExercises: GetExercises,
    val getTotalBodyParts: GetTotalBodyParts,
    val getTotalBodyPartsByWorkoutType: GetTotalBodyPartsByWorkoutType,
    val getTotalExercises: GetTotalExercises,
    val getTotalWorkouts: GetTotalWorkouts,
    val getWorkoutById: GetWorkoutById,
    val getWorkouts: GetWorkouts,
    val getWorkoutTypes: GetWorkoutTypes,
    val removeMultipleExercises: RemoveMultipleExercises,
    val removeMultipleWorkouts: RemoveMultipleWorkouts
) {
}