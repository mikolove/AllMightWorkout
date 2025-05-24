package com.mikolove.workout.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mikolove.core.domain.workout.GroupRepository
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.workout.domain.WorkoutRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


class WorkoutViewModel (
    private val workoutTypeRepository: WorkoutTypeRepository,
    private val groupRepository: GroupRepository,
    private val workoutRepository: WorkoutRepository,
) : ViewModel(){

    var state by mutableStateOf(WorkoutState())
        private set

    private val eventChannel = Channel<WorkoutEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

    }
}