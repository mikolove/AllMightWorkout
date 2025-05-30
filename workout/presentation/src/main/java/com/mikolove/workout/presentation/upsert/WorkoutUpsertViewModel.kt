package com.mikolove.workout.presentation.upsert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.map
import com.mikolove.core.domain.workout.GroupRepository
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.presentation.ui.asUiText
import com.mikolove.core.presentation.ui.mapper.toGroup
import com.mikolove.core.presentation.ui.mapper.toGroupUi
import com.mikolove.workout.domain.WorkoutRepository
import com.mikolove.workout.presentation.navigation.WorkoutUpsertRoute
import kotlinx.coroutines.channels.Channel
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
                if(state.workoutId.isNotBlank()){
                    when (val cacheWorkout = workoutRepository.getWorkout(state.workoutId)) {
                        is Result.Error -> {
                            eventChannel.send(WorkoutUpsertEvent.Error(cacheWorkout.error.asUiText()))
                        }
                        is Result.Success -> {

                            val workout = cacheWorkout.data

                            state.nameSelected.edit { append(workout.name) }

                            val listOfGroupSelected = workout.groups.map { group -> group.idGroup}

                            if(groups.isNotEmpty()){
                                val groupsUi = groups.map { groupUi ->
                                    if(groupUi.idGroup in listOfGroupSelected ){
                                        groupUi.copy(selected = false)
                                    }else{
                                        groupUi
                                    }}

                                state = state.copy(
                                    groups = groupsUi,
                                    groupsSelected = workout.groups.toMutableStateList())
                            }
                        }
                    }
                }else{
                    state = state.copy(groups = groups)
                }
            }.launchIn(viewModelScope)

    }

    fun onAction(action: WorkoutUpsertAction){
        when(action){
            is WorkoutUpsertAction.OnGroupClick -> {
                switchChipState(action.chipId)
            }
            is WorkoutUpsertAction.OnUpsertClick -> {
                if(state.workoutId.isNotBlank())
                    updateWorkout()
                else
                    createWorkout()
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

    private fun createWorkout(){

    }

    private fun updateWorkout(){
        viewModelScope.launch {

        /*    val cacheWorkout = workoutRepository.getWorkout(state.workoutId).map { it }
                is Result.Error -> {
                    eventChannel.send(WorkoutUpsertEvent.Error(cacheWorkout.error.asUiText()))
                }
                is Result.Success -> {
                    val workout = cacheWorkout.data
                    workout.name = state.nameSelected.text.toString().trim()
                    workout.groups = state.groupsSelected.toList()
                    workoutRepository.upsertWorkout(workout)
                }*/

            }
        }

}