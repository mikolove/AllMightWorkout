package com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WorkoutInteractionManager{

    private val _workoutNameState: MutableLiveData<WorkoutInteractionState>
            = MutableLiveData(WorkoutInteractionState.DefaultState())

    val workoutNameState: LiveData<WorkoutInteractionState>
        get() = _workoutNameState


    fun setWorkoutNameState(state: WorkoutInteractionState){
        if(!workoutNameState.toString().equals(state.toString())){
            _workoutNameState.value = state
            when(state){
                is WorkoutInteractionState.EditState -> {
                    //Switch other edit text state
                }
            }
        }
    }


    fun isEditingName() = workoutNameState.value.toString().equals(
        WorkoutInteractionState.EditState().toString()
    )


    fun exitEditState(){
        _workoutNameState.value = WorkoutInteractionState.DefaultState()
    }

    // return true if either title or body are in EditState
    fun checkEditState() = workoutNameState.value.toString().equals(
        WorkoutInteractionState.EditState().toString()
    )

}