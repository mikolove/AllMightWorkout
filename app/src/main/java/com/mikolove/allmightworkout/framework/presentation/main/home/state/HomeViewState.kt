package com.mikolove.allmightworkout.framework.presentation.main.home.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeViewState(

    //Retrieving list
    var listWorkouts: ArrayList<Workout>? = null,
    var listTrainingWorkouts: ArrayList<Workout>? = null, //Not sure if needed
    var listExercises: ArrayList<Exercise>? = null,
    var listWorkoutTypes: ArrayList<WorkoutType>? = null,
    var listBodyParts: ArrayList<BodyPart>? = null,
    var listExercisesFromWorkoutId: ArrayList<Exercise>? = null, //Not sure if needed

    //Workout selected for training
    var workoutSelected: Workout? = null,
    var workoutTypeSelected : WorkoutType? = null,

    //Search option
    var searchQueryWorkouts: String? = null,
    var pageWorkouts: Int? = null,
    var isQueryExhaustedWorkouts: Boolean? = null,
    var searchQueryExercises: String? = null,
    var pageExercises: Int? = null,
    var isQueryExhaustedExercises: Boolean? = null,
/*
    var searchQueryTrainingWorkouts: String? = null,
    var pageTrainingWorkouts: Int? = null,
    var isQueryExhaustedTrainingWorkouts: Boolean? = null,
*/

    //Load and store filters for each list
    var workout_list_filter: String? = null,
    var workout_list_order: String? = null,
    var exercise_list_filter: String? = null,
    var exercise_list_order: String? = null,
    var load_workout_list_order: String? = null,
    var load_workout_list_filter: String? = null,

    //clean UI
    var layoutManagerState: Parcelable? = null,

    //Total elements in cache for each list - maybe undeeded
    var totalExercises: Int? = null,
    var totalWorkouts: Int? = null,
    var totalBodyParts: Int? = null,
    var totalBodyPartsByWorkoutType: Int? = null

) : Parcelable, ViewState {


}