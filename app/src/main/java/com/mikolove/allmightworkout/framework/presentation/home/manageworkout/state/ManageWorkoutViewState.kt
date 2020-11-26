package com.mikolove.allmightworkout.framework.presentation.home.manageworkout.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManageWorkoutViewState(
    var workout : Workout? = null,
    var exerciseList : ArrayList<Exercise>? = null,
    var newWorkout: Workout? = null,
    var isQueryExhausted: Boolean? = null,
    var isUpdatePending : Boolean? = null,
    var layoutManagerState: Parcelable? = null
) : Parcelable, ViewState {

}