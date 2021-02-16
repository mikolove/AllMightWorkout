package com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.WorkoutInteractionState.*

class WorkoutInteractionManager{

    private val _workoutNameState: MutableLiveData<WorkoutInteractionState>
            = MutableLiveData(DefaultState())

    val workoutNameState: LiveData<WorkoutInteractionState>
        get() = _workoutNameState


    fun setWorkoutNameState(state: WorkoutInteractionState){
        if(!workoutNameState.toString().equals(state.toString())){
            _workoutNameState.value = state
            when(state){
                is EditState -> {
                    //Switch other edit text state
                }
            }
        }
    }

    fun isEditingName() = workoutNameState.value.toString().equals(
        EditState().toString()
    )

    fun exitEditState(){
        _workoutNameState.value = DefaultState()
    }

    // return true if either name or something else is in EditState
    fun checkEditState() = workoutNameState.value.toString().equals(
        EditState().toString()
    )

}