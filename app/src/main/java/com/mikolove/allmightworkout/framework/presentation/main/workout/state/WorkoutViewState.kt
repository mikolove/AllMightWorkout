package com.mikolove.allmightworkout.framework.presentation.main.workout.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkoutViewState(
    var workout: Workout? = null,
    var historyWorkout: HistoryWorkout? = null,
    var historyExercises: ArrayList<HistoryExercise>? = null,
    var layoutManagerState: Parcelable? = null
) : Parcelable, ViewState {
}