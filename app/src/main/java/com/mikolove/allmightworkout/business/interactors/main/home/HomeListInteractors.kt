package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.business.interactors.main.exercise.GetExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.GetTotalExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.business.interactors.main.workout.GetTotalWorkouts
import com.mikolove.allmightworkout.business.interactors.main.workout.GetWorkouts
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts

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