package com.mikolove.workout.presentation.detail

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.mikolove.workout.domain.WorkoutRepository
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class WorkoutDetailViewModel(
    private val workoutRepository: WorkoutRepository,
) : ViewModel(){

    var state by mutableStateOf(WorkoutDetailState())
        private set

    private val eventChannel = Channel<WorkoutDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

    }

    fun onAction(action: WorkoutDetailAction){
        when(action){
            else -> {}
        }
    }

}