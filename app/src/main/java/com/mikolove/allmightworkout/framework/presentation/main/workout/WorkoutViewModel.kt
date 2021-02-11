package com.mikolove.allmightworkout.framework.presentation.main.workout

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel
@Inject
constructor(
    private val workoutListInteractors: WorkoutListInteractors,
    private val historyWorkoutFactory : HistoryWorkoutFactory,
    private val historyExerciseFactory: HistoryExerciseFactory,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
    ) : BaseViewModel<WorkoutViewState>() {


    override fun handleNewData(data: WorkoutViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): WorkoutViewState {
        return WorkoutViewState()
    }
}