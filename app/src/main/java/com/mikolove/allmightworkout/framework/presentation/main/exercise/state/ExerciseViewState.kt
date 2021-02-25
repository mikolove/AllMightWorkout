package com.mikolove.allmightworkout.framework.presentation.main.exercise.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseViewState(

    //Retrieving list
    var listExercises: ArrayList<Exercise>? = null,
    var listWorkoutTypes: ArrayList<WorkoutType>? = null,
    var listBodyParts: ArrayList<BodyPart>? = null,

    //Insert workout
    var exerciseToInsert : Exercise? = null,

    //Update
    var isUpdatePending : Boolean? = null,

    //Workout selected for training
    var exerciseSelected: Exercise? = null,
    var workoutTypeSelected : WorkoutType? = null,

    //Search option
    var searchActive : Boolean? = null,
    var searchQueryExercises: String? = null,
    var pageExercises: Int? = null,
    var isExercisesQueryExhausted: Boolean? = null,


    //Load and store filters for each list
    var exercise_list_filter: String? = null,
    var exercise_list_order: String? = null,

    //Total elements in cache for each list - maybe undeeded
    var totalExercises: Int? = null,
    var totalBodyParts: Int? = null,
    var totalBodyPartsByWorkoutType: Int? = null

) : Parcelable, ViewState {
}