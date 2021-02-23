package com.mikolove.allmightworkout.framework.presentation.main.workout.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutViewState(

    //Retrieving lists
    var listWorkouts: ArrayList<Workout>? = null,
    var listWorkoutTypes: ArrayList<WorkoutType>? = null,
    var listBodyParts: ArrayList<BodyPart>? = null,
    var listExercisesFromWorkoutId: ArrayList<Exercise>? = null, //Not sure if needed

    //Insert workout
    var workoutToInsert : Workout? = null,

    //Update
    var isUpdatePending : Boolean? = null,

    //Add/Remove
    var lastWorkoutExerciseState : Boolean? = null,

    //Workout selected for training
    var workoutSelected: Workout? = null,
    var workoutTypeSelected : WorkoutType? = null,

    //Search option
    var searchActive : Boolean? = null,
    var searchQueryWorkouts: String? = null,
    var pageWorkouts: Int? = null,
    var isWorkoutsQueryExhausted: Boolean? = null,


    //Load and store filters for each list
    var workout_list_filter: String? = null,
    var workout_list_order: String? = null,

    //clean UI
    var workoutDetailRecyclerLayoutManagerState: Parcelable? = null,

    //Total elements in cache for each list - maybe undeeded
    var totalWorkouts: Int? = null,
    var totalBodyParts: Int? = null,
    var totalBodyPartsByWorkoutType: Int? = null

): Parcelable, ViewState {


}