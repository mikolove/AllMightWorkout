package com.mikolove.allmightworkout.framework.presentation.main.workout

import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel
@Inject
constructor(
    private val workoutInteractors: WorkoutInteractors
) : BaseViewModel<WorkoutViewState>(){

    override fun initNewViewState(): WorkoutViewState {
        return WorkoutViewState()
    }

    override fun handleNewData(data: WorkoutViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }



}