package com.mikolove.exercise.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.domain.util.Result
import com.mikolove.core.presentation.ui.mapper.toWorkoutTypeUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val workoutTypeRepository: WorkoutTypeRepository
) : ViewModel(){

    var state by mutableStateOf(ExerciseState())
        private set

    private val eventChannel = Channel<ExerciseEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        viewModelScope.launch {
            val result = workoutTypeRepository.getWorkoutTypes()
            if(result is Result.Success){
                val list = result.data.map { it.toWorkoutTypeUI() }
                state = state.copy(workoutTypes = result.data.map { it.toWorkoutTypeUI() }.sortedBy { it.name })
            }
        }

    }

    fun onAction(action: ExerciseAction){
        when(action) {
            else -> {}
        }
    }

}