package com.mikolove.allmightworkout.framework.presentation.main.exercise_set

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.exercise_set.ExerciseSetInteractionState.*

class ExerciseSetInteractionManager{

    private val _weightState : MutableLiveData<ExerciseSetInteractionState>
            = MutableLiveData(DefaultState())

    val weightState : LiveData<ExerciseSetInteractionState>
        get() = _weightState

    private val _repState : MutableLiveData<ExerciseSetInteractionState>
            = MutableLiveData(DefaultState())

    val repState : LiveData<ExerciseSetInteractionState>
        get() = _repState

    private val _timeState : MutableLiveData<ExerciseSetInteractionState>
            = MutableLiveData(DefaultState())

    val timeState : LiveData<ExerciseSetInteractionState>
        get() = _timeState


    private val _restState : MutableLiveData<ExerciseSetInteractionState>
            = MutableLiveData(DefaultState())

    val restState : LiveData<ExerciseSetInteractionState>
        get() = _restState

    fun setRepState(state: ExerciseSetInteractionState){
        if(!repState.toString().equals(state.toString())){
            _repState.value = state
            when(state){
                is EditState ->{
                    _weightState.value = DefaultState()
                    _timeState.value = DefaultState()
                    _restState.value = DefaultState()

                }
            }
        }
    }

    fun setWeightState(state: ExerciseSetInteractionState){
        if(!weightState.toString().equals(state.toString())){
            _weightState.value = state
            when(state){
                is EditState ->{
                    _repState.value = DefaultState()
                    _timeState.value = DefaultState()
                    _restState.value = DefaultState()
                }
            }
        }
    }

    fun setTimeState(state: ExerciseSetInteractionState){
        if(!timeState.toString().equals(state.toString())){
            _timeState.value = state
            when(state){
                is EditState ->{
                    _repState.value = DefaultState()
                    _weightState.value = DefaultState()
                    _restState.value = DefaultState()
                }
            }
        }
    }

    fun setRestState(state: ExerciseSetInteractionState){
        if(!restState.toString().equals(state.toString())){
            _restState.value = state
            when(state){
                is EditState ->{
                    _repState.value = DefaultState()
                    _timeState.value = DefaultState()
                    _weightState.value = DefaultState()
                }
            }
        }
    }
    fun isEditingRep() = repState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingWeight() = weightState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingTime() = timeState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingRest() = restState.value.toString().equals(
        EditState().toString()
    )

    fun exitEditState(){
        _repState.value = DefaultState()
        _weightState.value = DefaultState()
        _timeState.value = DefaultState()
        _restState.value = DefaultState()
    }

    fun checkEditState() =
        repState.value.toString().equals(EditState().toString()) ||
        weightState.value.toString().equals(EditState().toString()) ||
        timeState.value.toString().equals(EditState().toString()) ||
        restState.value.toString().equals(EditState().toString())
}