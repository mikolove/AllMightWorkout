package com.mikolove.workout.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workout.GroupRepository
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.presentation.ui.mapper.toGroupUi
import com.mikolove.core.presentation.ui.mapper.toIds
import com.mikolove.core.presentation.ui.mapper.toWorkoutTypeUI
import com.mikolove.core.presentation.ui.mapper.toWorkoutUi
import com.mikolove.workout.domain.WorkoutRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


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

        viewModelScope.launch {
            val resultWorkoutTypes = workoutTypeRepository.getWorkoutTypes()
            if (resultWorkoutTypes is Result.Success) {
                state =
                    state.copy(workoutTypes = resultWorkoutTypes.data.map { it.toWorkoutTypeUI() }
                        .sortedBy { it.name })
            }
        }

        groupRepository.getGroups()
            .map { groups -> groups.map { it.toGroupUi() } }
            .onEach{ groups ->
                state = state.copy(groups = groups)
            }.launchIn(viewModelScope)

        combine(
            snapshotFlow { state.workoutTypes },
            snapshotFlow { state.groups },
        ) {  workoutTypes, groups ->
            val filteredWorkoutTypes = workoutTypes.toIds()
            val filteredGroups = groups.toIds()

            if(groups.isEmpty()){
                workoutRepository.getWorkoutsWithExercises(filteredWorkoutTypes).onEach { exercises ->
                    val workoutsUi = exercises.map { it.toWorkoutUi() }
                    state = state.copy(workouts = workoutsUi)
                }
            }else{
                workoutRepository.getWorkoutsWithExercisesWithGroups(filteredWorkoutTypes,filteredGroups).onEach { exercises ->
                    val workoutsUi = exercises.map { it.toWorkoutUi() }
                    state = state.copy(workouts = workoutsUi)
                }
            }
        }.launchIn(viewModelScope)

    }


    fun onAction(action: WorkoutAction){
        when(action) {
            is WorkoutAction.onWorkoutTypeChipClick ->{
                switchWorkoutTypeChipState(action.chipId)
            }
            is WorkoutAction.onGroupChipClick->{
                switchGroupChipState(action.chipId)
            }
            else -> {}
        }
    }

    private fun switchWorkoutTypeChipState(chipId : String?) {

        val updatedFilters = state.workoutTypes.map { wkTypeUI ->
            if(chipId.isNullOrEmpty()){
                wkTypeUI.copy(selected = false)
            }else{
                if(wkTypeUI.idWorkoutType == chipId){
                    wkTypeUI.copy(selected = !wkTypeUI.selected)
                }else{
                    wkTypeUI
                }
            }
        }

        state = state.copy(workoutTypes = updatedFilters)
    }

    private fun switchGroupChipState(chipId : String?) {

        val updatedFilters = state.groups.map { groupUi ->
            if(chipId.isNullOrEmpty()){
                groupUi.copy(selected = false)
            }else{
                if(groupUi.idGroup == chipId){
                    groupUi.copy(selected = !groupUi.selected)
                }else{
                    groupUi
                }
            }
        }

        state = state.copy(groups = updatedFilters)
    }

}