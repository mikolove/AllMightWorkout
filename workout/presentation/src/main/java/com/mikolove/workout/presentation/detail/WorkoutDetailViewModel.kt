package com.mikolove.workout.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mikolove.core.presentation.ui.mapper.toWorkoutUi
import com.mikolove.workout.domain.WorkoutRepository
import com.mikolove.workout.presentation.navigation.WorkoutDetailRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class WorkoutDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
) : ViewModel(){


    /*var state by mutableStateOf(WorkoutDetailState(workoutId = savedStateHandle.toRoute<WorkoutDetailRoute>().idWorkout))
        private set*/
    private val _state  = savedStateHandle.getMutableStateFlow(
        key = "wd_state",
        initialValue = WorkoutDetailState(workoutId = savedStateHandle.toRoute<WorkoutDetailRoute>().idWorkout)
    )
    val state = _state.asStateFlow()

    private val eventChannel = Channel<WorkoutDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        //Save current ID
        //savedStateHandle["wd_state"] = _state.value.workoutId

        workoutRepository.getWorkout(_state.value.workoutId)
            .map { it.toWorkoutUi() }
            .onEach { workoutUi ->
                _state.update {
                    it.copy(workoutUi = workoutUi)
                }
            }.launchIn(viewModelScope)

    }

    fun onAction(action: WorkoutDetailAction){
        when(action){
            else -> {}
        }
    }

}