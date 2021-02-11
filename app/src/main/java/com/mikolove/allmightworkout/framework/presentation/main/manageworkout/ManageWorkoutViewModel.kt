package com.mikolove.allmightworkout.framework.presentation.main.manageworkout

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.ManageWorkoutListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageWorkoutViewModel
@Inject
constructor(
    private val manageWorkoutListInteractors: ManageWorkoutListInteractors,
    private val workoutFactory: WorkoutFactory,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetFactory: ExerciseSetFactory,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<ManageWorkoutViewState>(){

    override fun handleNewData(data: ManageWorkoutViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): ManageWorkoutViewState {
        return ManageWorkoutViewState()
    }
}