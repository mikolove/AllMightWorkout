package com.mikolove.allmightworkout.framework.presentation.main.history.state

import android.os.Parcelable
import com.mikolove.allmightworkout.business.domain.model.HistoryExercise
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSet
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
class HistoryViewState(
    var listHistoryWorkouts : ArrayList<HistoryWorkout>? = null,
    var historyWorkoutDetail : HistoryWorkout? = null,
    var listHistoryExercise:  ArrayList<HistoryExercise>? = null,
    var listHistoryExerciseSet:  ArrayList<HistoryExerciseSet>? = null,
    var historyWorkoutId : String? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var filter: String? = null,
    var order: String? = null,
    var layoutManagerState: Parcelable? = null
    ) : Parcelable, ViewState{
}