package com.mikolove.allmightworkout.framework.presentation.home.chooseworkout.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseWorkoutViewState(
    var workoutList: ArrayList<Workout>? = null,
    var workoutTypeList : ArrayList<WorkoutType>? = null,
    var bodyPartList : ArrayList<BodyPart>? = null,
    var searchQuery: String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null,
    var numWorkoutInCache: Int? = null

) : Parcelable, ViewState {

}
