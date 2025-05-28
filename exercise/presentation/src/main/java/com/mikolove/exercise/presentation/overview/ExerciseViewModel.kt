@file:Suppress("OPT_IN_USAGE")

package com.mikolove.exercise.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.presentation.ui.mapper.toWorkoutTypeUI
import com.mikolove.exercise.domain.ExerciseRepository
import com.mikolove.core.presentation.ui.mapper.toExerciseUi
import com.mikolove.core.presentation.ui.mapper.toIds
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val workoutTypeRepository: WorkoutTypeRepository,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel(){

    var state by mutableStateOf(ExerciseState())
        private set

    private val eventChannel = Channel<ExerciseEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        viewModelScope.launch {
            val result = workoutTypeRepository.getWorkoutTypes()
            if(result is Result.Success){
                state = state.copy(workoutTypes = result.data.map { it.toWorkoutTypeUI() }.sortedBy { it.name })
            }
        }

        snapshotFlow { state.workoutTypes }
            .flatMapLatest {  workoutTypes ->
                val filteredWorkoutTypes = workoutTypes.toIds()

                exerciseRepository.getExercisesByWorkoutTypes(filteredWorkoutTypes).onEach { exercises ->
                    val exercisesUi = exercises.map { it.toExerciseUi() }
                    state = state.copy(exercises = exercisesUi)
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            exerciseRepository.syncPendingExercises()
            exerciseRepository.fetchExercises()
        }


    }

    fun onAction(action: ExerciseAction){
        when(action) {
            is ExerciseAction.onChipClick ->{
                switchChipState(action.chipId)
            }
            else -> {}
        }
    }

    private fun switchChipState(chipId : String?) {

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

}