package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.InsertWorkout
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState

class HomeListInteractors(
    val insertWorkout: InsertWorkout,
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