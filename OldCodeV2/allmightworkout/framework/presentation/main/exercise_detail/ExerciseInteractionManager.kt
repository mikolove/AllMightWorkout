package com.mikolove.allmightworkout.framework.presentation.main.exercise_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.framework.presentation.main.exercise_detail.ExerciseInteractionState.*

class ExerciseInteractionManager {

    private val _nameState : MutableLiveData<ExerciseInteractionState>
        = MutableLiveData(DefaultState())

    val nameState : LiveData<ExerciseInteractionState>
        get() = _nameState

    private val _isActiveState : MutableLiveData<ExerciseInteractionState>
        = MutableLiveData(DefaultState())

    val isActiveState : LiveData<ExerciseInteractionState>
        get() = _isActiveState

    private val _workoutTypeState : MutableLiveData<ExerciseInteractionState>
            = MutableLiveData(DefaultState())

    val workoutTypeState : LiveData<ExerciseInteractionState>
        get() = _workoutTypeState


    private val _bodyPartState : MutableLiveData<ExerciseInteractionState>
            = MutableLiveData(DefaultState())

    val bodyPartState : LiveData<ExerciseInteractionState>
        get() = _bodyPartState


    private val _exerciseTypeState : MutableLiveData<ExerciseInteractionState>
            = MutableLiveData(DefaultState())

    val exerciseTypeState : LiveData<ExerciseInteractionState>
        get() = _exerciseTypeState


    fun setNameState(state: ExerciseInteractionState){
        if(!nameState.toString().equals(state.toString())){
            _nameState.value = state
            when(state){
                is EditState ->{
                    _isActiveState.value = DefaultState()
                    _workoutTypeState.value = DefaultState()
                    _bodyPartState.value = DefaultState()
                    _exerciseTypeState.value = DefaultState()
                }
                is DefaultState -> TODO()
            }
        }
    }

    fun setIsActiveState(state: ExerciseInteractionState){
        if(!isActiveState.toString().equals(state.toString())){
            _isActiveState.value = state
            when(state){
                is EditState ->{
                    _nameState.value = DefaultState()
                    _workoutTypeState.value = DefaultState()
                    _bodyPartState.value = DefaultState()
                    _exerciseTypeState.value = DefaultState()
                }
                is DefaultState -> TODO()
            }
        }
    }

    fun setBodyPartState(state: ExerciseInteractionState){
        if(!bodyPartState.toString().equals(state.toString())){
            _bodyPartState.value = state
            when(state){
                is EditState ->{
                    _isActiveState.value = DefaultState()
                    _workoutTypeState.value = DefaultState()
                    _nameState.value = DefaultState()
                    _exerciseTypeState.value = DefaultState()
                }
                is DefaultState -> TODO()
            }
        }
    }

    fun setExerciseTypeState(state: ExerciseInteractionState){
        if(!exerciseTypeState.toString().equals(state.toString())){
            _exerciseTypeState.value = state
            when(state){
                is EditState ->{
                    _isActiveState.value = DefaultState()
                    _workoutTypeState.value = DefaultState()
                    _bodyPartState.value = DefaultState()
                    _nameState.value = DefaultState()
                }
                is DefaultState -> TODO()
            }
        }
    }

    fun setWorkoutTypeState(state: ExerciseInteractionState){
        if(!workoutTypeState.toString().equals(state.toString())){
            _workoutTypeState.value = state
            when(state){
                is EditState ->{
                    _isActiveState.value = DefaultState()
                    _exerciseTypeState.value = DefaultState()
                    _bodyPartState.value = DefaultState()
                    _nameState.value = DefaultState()
                }
                is DefaultState -> TODO()
            }
        }
    }

    fun isEditingName() = nameState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingIsActive() = isActiveState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingBodyPart() = bodyPartState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingExerciseType() = exerciseTypeState.value.toString().equals(
        EditState().toString()
    )

    fun isEditingWorkoutType() = workoutTypeState.value.toString().equals(
        EditState().toString()
    )

    fun exitEditState(){
        _nameState.value = DefaultState()
        _workoutTypeState.value = DefaultState()
        _bodyPartState.value = DefaultState()
        _isActiveState.value = DefaultState()
        _exerciseTypeState.value = DefaultState()
    }

    fun checkEditState() =
        nameState.value.toString().equals(EditState().toString()) ||
        workoutTypeState.value.toString().equals(EditState().toString()) ||
        bodyPartState.value.toString().equals(EditState().toString()) ||
        isActiveState.value.toString().equals(EditState().toString()) ||
        exerciseTypeState.value.toString().equals(EditState().toString())
}