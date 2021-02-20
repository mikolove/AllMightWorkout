package com.mikolove.allmightworkout.framework.presentation.main.manageexercise

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.BodyPartFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.model.WorkoutTypeFactory
import com.mikolove.allmightworkout.business.domain.state.StateEvent
import com.mikolove.allmightworkout.business.interactors.main.exercise.ManageExerciseListInteractors
import com.mikolove.allmightworkout.framework.presentation.common.BaseViewModel
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageExerciseViewModel
@Inject
constructor(
    private val manageExerciseListInteractors: ManageExerciseListInteractors,
    private val exerciseFactory: ExerciseFactory,
    private val exerciseSetFactory : ExerciseSetFactory,
    private val bodyPartFactory : BodyPartFactory,
    private val workoutTypeFactory: WorkoutTypeFactory,
    private val editor : SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<ManageExerciseViewState>(){

    override fun handleNewData(data: ManageExerciseViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

    override fun initNewViewState(): ManageExerciseViewState {
        return ManageExerciseViewState()
    }
}