package com.mikolove.allmightworkout.framework.presentation.main.exercise

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel
@Inject
constructor(
    private val exerciseInteractors: ExerciseInteractors,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences,
    private val exerciseFactory: ExerciseFactory
) : BaseViewModel<ExerciseViewState>()
{
    override fun handleNewData(data: ExerciseViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): ExerciseViewState {
        return ExerciseViewState()
    }
}

