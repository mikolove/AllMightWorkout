package com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManageWorkoutViewState(

    var cacheIdWorkout : String?  = null,
    var workout : Workout? = null,
    var exerciseList : List<Exercise>? = null,

    var lastWorkoutExerciseState : Boolean? = null,
    var layoutManagerState: Parcelable? = null
) : Parcelable, ViewState {

}