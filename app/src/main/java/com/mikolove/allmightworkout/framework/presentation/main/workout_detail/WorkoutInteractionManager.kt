package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.workout_detail.WorkoutInteractionState.*

class WorkoutInteractionManager{

    private val _workoutNameState: MutableLiveData<WorkoutInteractionState>
            = MutableLiveData(DefaultState())

    val workoutNameState: LiveData<WorkoutInteractionState>
        get() = _workoutNameState

    private val _workoutIsActiveState: MutableLiveData<WorkoutInteractionState>
            = MutableLiveData(DefaultState())

    val workoutIsActiveState: LiveData<WorkoutInteractionState>
        get() = _workoutIsActiveState


    fun setWorkoutNameState(state: WorkoutInteractionState){
        if(!workoutNameState.toString().equals(state.toString())){
            _workoutNameState.value = state
            when(state){
                is EditState -> {
                    //Switch other edit State
                    _workoutIsActiveState.value = DefaultState()
                }
            }
        }
    }

    fun setWorkoutIsActiveState(state: WorkoutInteractionState){
        if(!workoutIsActiveState.toString().equals(state.toString())){
            _workoutIsActiveState.value = state
            when(state){
                is EditState -> {
                    //Switch other edit State
                    _workoutNameState.value = DefaultState()
                }
            }
        }
    }

    fun isEditingName() = workoutNameState.value.toString().equals(
        EditState().toString()
    )
    fun isEditingIsActive() = workoutIsActiveState.value.toString().equals(
        EditState().toString()
    )

    fun exitEditState(){
        _workoutNameState.value = DefaultState()
        _workoutIsActiveState.value = DefaultState()
    }

    // return true if either name or something else is in EditState
    fun checkEditState() = workoutNameState.value.toString().equals(EditState().toString()) ||
        workoutIsActiveState.value.toString().equals(EditState().toString())


}