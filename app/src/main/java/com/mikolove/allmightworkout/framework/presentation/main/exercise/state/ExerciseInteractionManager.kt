package com.mikolove.allmightworkout.framework.presentation.main.exercise.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseInteractionState.*

class ExerciseInteractionManager {

    private val _exerciseNameState : MutableLiveData<ExerciseInteractionState>
        = MutableLiveData(DefaultState())

    val exerciseNameState : LiveData<ExerciseInteractionState>
        get() = _exerciseNameState

    private val _exerciseIsActiveState : MutableLiveData<ExerciseInteractionState>
        = MutableLiveData(DefaultState())

    val exerciseIsActiveState : LiveData<ExerciseInteractionState>
        get() = _exerciseIsActiveState

    private val _exerciseBodyPartState : MutableLiveData<ExerciseInteractionState>
            = MutableLiveData(DefaultState())

    val exerciseBodyPartState : LiveData<ExerciseInteractionState>
        get() = _exerciseBodyPartState


    private val _exerciseExerciseTypeState : MutableLiveData<ExerciseInteractionState>
            = MutableLiveData(DefaultState())

    val exerciseExerciseTypeState : LiveData<ExerciseInteractionState>
        get() = _exerciseExerciseTypeState


    fun setExerciseNameState(state: ExerciseInteractionState){
        if(!exerciseNameState.toString().equals(state.toString())){
            _exerciseNameState.value = state
            when(state){
                is EditState ->{
                    _exerciseIsActiveState.value = DefaultState()
                    _exerciseBodyPartState.value = DefaultState()
                    _exerciseExerciseTypeState.value = DefaultState()
                }
            }
        }
    }

    fun setExerciseIsActiveState(state: ExerciseInteractionState){
        if(!exerciseIsActiveState.toString().equals(state.toString())){
            _exerciseIsActiveState.value = state
            when(state){
                is EditState ->{
                    _exerciseNameState.value = DefaultState()
                    _exerciseBodyPartState.value = DefaultState()
                    _exerciseExerciseTypeState.value = DefaultState()
                }
            }
        }
    }

    fun setExerciseBodyPartState(state: ExerciseInteractionState){
        if(!exerciseBodyPartState.toString().equals(state.toString())){
            _exerciseBodyPartState.value = state
            when(state){
                is EditState ->{
                    _exerciseIsActiveState.value = DefaultState()
                    _exerciseNameState.value = DefaultState()
                    _exerciseExerciseTypeState.value = DefaultState()
                }
            }
        }
    }

    fun setExerciseExerciseTypeState(state: ExerciseInteractionState){
        if(!exerciseExerciseTypeState.toString().equals(state.toString())){
            _exerciseExerciseTypeState.value = state
            when(state){
                is EditState ->{
                    _exerciseIsActiveState.value = DefaultState()
                    _exerciseBodyPartState.value = DefaultState()
                    _exerciseNameState.value = DefaultState()
                }
            }
        }
    }

    fun isEditingName() = exerciseNameState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingIsActive() = exerciseIsActiveState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingBodyPart() = exerciseBodyPartState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingExerciseType() = exerciseExerciseTypeState.value.toString().equals(
        EditState().toString()
    )

    fun exitEditState(){
        _exerciseNameState.value = DefaultState()
        _exerciseBodyPartState.value = DefaultState()
        _exerciseIsActiveState.value = DefaultState()
        _exerciseExerciseTypeState.value = DefaultState()
    }

    fun checkEditState() =
        exerciseNameState.value.toString().equals(EditState().toString()) ||
        exerciseBodyPartState.value.toString().equals(EditState().toString()) ||
        exerciseIsActiveState.value.toString().equals(EditState().toString()) ||
        exerciseExerciseTypeState.value.toString().equals(EditState().toString())
}