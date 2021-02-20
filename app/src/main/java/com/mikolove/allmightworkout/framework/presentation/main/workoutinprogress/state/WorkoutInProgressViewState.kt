package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
class WorkoutInProgressViewState(
    var workout: Workout? = null,
    var lastHistoryWorkoutInserted: HistoryWorkout? = null,
    var lastHistoryExerciseInserted: HistoryExercise? = null,
    var lastHistoryExerciseSetInserted: HistoryExerciseSet? = null,
    var historyExercises: ArrayList<HistoryExercise>? = null,
    var layoutManagerState: Parcelable? = null
) : Parcelable, ViewState {
}