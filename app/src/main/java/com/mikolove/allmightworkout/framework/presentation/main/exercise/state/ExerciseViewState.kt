package com.mikolove.allmightworkout.framework.presentation.main.exercise.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize
import retrofit2.http.Body

@Parcelize
data class ExerciseViewState(

    //Retrieving list
    var listExercises: ArrayList<Exercise>? = null,
    var listWorkoutTypes: ArrayList<WorkoutType>? = null,
    var listBodyParts: ArrayList<BodyPart>? = null,
    var cachedExerciseSetsByIdExercise : ArrayList<ExerciseSet>? = null,

    //Exercise Management
    var isExistExercise : Boolean? = null,
    var exerciseSelected: Exercise? = null,

    //Update
    var isUpdatePending : Boolean? = null,

    //Exercise Form selected option
    var workoutTypeSelected : WorkoutType? = null,

    //Search option
    var searchActive : Boolean? = null,
    var searchQueryExercises: String? = null,
    var pageExercises: Int? = null,


    //Query exhausted
    var isExercisesQueryExhausted: Boolean? = null,
    var isWorkoutTypesExhausted : Boolean? = null,
    var isCachedExercisesSetsExhausted : Boolean? = null,

    //Load and store filters for each list
    var exercise_list_filter: String? = null,
    var exercise_list_order: String? = null,

    //Total elements in cache for each list - maybe undeeded
    var totalExercises: Int? = null,
    var totalBodyParts: Int? = null,
    var totalBodyPartsByWorkoutType: Int? = null

) : Parcelable, ViewState {
}