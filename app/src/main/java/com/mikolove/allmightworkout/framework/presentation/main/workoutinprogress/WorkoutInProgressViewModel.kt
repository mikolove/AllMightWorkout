package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.WorkoutInProgressListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutInProgressViewModel
@Inject
constructor(
    private val workoutInProgressListInteractors: WorkoutInProgressListInteractors,
    private val historyWorkoutFactory : HistoryWorkoutFactory,
    private val historyExerciseFactory: HistoryExerciseFactory,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
    ) : BaseViewModel<WorkoutInProgressViewState>() {


    override fun handleNewData(data: WorkoutInProgressViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): WorkoutInProgressViewState {
        return WorkoutInProgressViewState()
    }
}