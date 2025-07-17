package com.mikolove.workout.presentation.upsert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workout.GroupRepository
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.presentation.ui.asUiText
import com.mikolove.core.presentation.ui.mapper.toGroup
import com.mikolove.core.presentation.ui.mapper.toGroupUi
import com.mikolove.workout.domain.WorkoutRepository
import com.mikolove.workout.presentation.navigation.WorkoutUpsertRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkoutUpsertViewModel(
    private val workoutRepository: WorkoutRepository,
    groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    var state by mutableStateOf(WorkoutUpsertState(workoutId = savedStateHandle.toRoute<WorkoutUpsertRoute>().idWorkout))
        private set

    private val eventChannel = Channel<WorkoutUpsertEvent>()
    val events  = eventChannel.receiveAsFlow()

    init {

        //Load groups
        groupRepository.getGroups()
            .map { groups -> groups.map { it.toGroupUi() } }
            .onEach { groups ->
                if (state.workoutId.isNotBlank()) {
                    when (val cacheWorkout = workoutRepository.getWorkoutById(state.workoutId)) {
                        is Result.Error -> {
                            eventChannel.send(WorkoutUpsertEvent.Error(cacheWorkout.error.asUiText()))
                        }

                        is Result.Success -> {

                            val workout = cacheWorkout.data

                            state = state.copy(
                                originalName = workout.name,
                                originalGroups = workout.groups,
                                originalCreatedAt = workout.createdAt
                            )

                            state.nameSelected.edit { append(workout.name) }

                            val listOfGroupSelected = workout.groups.map { group -> group.idGroup }

                            if (groups.isNotEmpty()) {
                                val groupsUi = groups.map { groupUi ->
                                    if (groupUi.idGroup in listOfGroupSelected) {
                                        groupUi.copy(selected = false)
                                    } else {
                                        groupUi
                                    }
                                }

                                state = state.copy(
                                    groups = groupsUi,
                                    groupsSelected = workout.groups.toMutableStateList()
                                )
                            }
                        }
                    }
                } else {
                    state = state.copy(groups = groups)
                }
            }.launchIn(viewModelScope)

        combine(
            snapshotFlow { state.nameSelected.text },
            snapshotFlow { state.groupsSelected.toList() }
            ) { name, groups ->

                state = if(name.isNotBlank()) {
                    state.copy(isWorkoutValid = true)
                }else {
                    state.copy(isWorkoutValid = false)
                }

        }.launchIn(viewModelScope)

    }

    fun onAction(action: WorkoutUpsertAction){
        when(action){
            is WorkoutUpsertAction.OnGroupClick -> {
                switchChipState(action.chipId)
            }
            is WorkoutUpsertAction.OnUpsertClick -> {
                upsertWorkout()
            }
            else -> Unit
        }
    }

    private fun switchChipState(chipId : String?) {

        val updatedBodyParts = state.groups.map { groupUi ->
            if(groupUi.idGroup == chipId){
                groupUi.copy(selected = !groupUi.selected)
            }else{
                groupUi
            }
        }

        state = state.copy(
            groups = updatedBodyParts,
            groupsSelected = updatedBodyParts.filter { it.selected }.map { it.toGroup() }.toMutableStateList()
        )
    }

    private fun upsertWorkout(){

        val workout = Workout(
            name = state.nameSelected.text.toString().trim(),
            groups = state.groupsSelected.toList(),
        )

        if(state.workoutId.isNotBlank()) {
            workout.idWorkout = state.workoutId
            workout.createdAt = state.originalCreatedAt
        }

        viewModelScope.launch {
            when(val cacheResult = workoutRepository.upsertWorkout(workout)){
                is Result.Error -> {
                    eventChannel.send(WorkoutUpsertEvent.Error(cacheResult.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(WorkoutUpsertEvent.OnFinish(workout.idWorkout))
                }
            }
        }
    }

}