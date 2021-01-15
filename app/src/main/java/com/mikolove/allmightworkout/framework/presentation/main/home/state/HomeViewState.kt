package com.mikolove.allmightworkout.framework.presentation.main.home.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.ViewState
import com.mikolove.allmightworkout.business.interactors.main.home.GetTotalBodyPartsByWorkoutType
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeViewState(
    var listWorkouts : ArrayList<Workout>? = null,
    var listExercises : ArrayList<Exercise>? = null,
    var listWorkoutTypes : ArrayList<WorkoutType>? = null,
    var listbodyParts : ArrayList<BodyPart>? = null,
    var listExercisesFromWorkoutId : ArrayList<Exercise>? = null,
    var workoutSelected: Workout? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numExercisesInCache: Int? = null,
    var numWorkoutInCache: Int? = null,
    var numBodyParts : Int? = null,
    var numBodyPartsByWorkoutType : Int? = null
) : Parcelable, ViewState {
}