package com.mikolove.exercise.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.presentation.ui.mapper.toWorkoutTypeUI
import com.mikolove.core.presentation.ui.model.BodyPartUi
import com.mikolove.exercise.domain.ExerciseRepository
import com.mikolove.exercise.presentation.mapper.toExerciseUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class ExerciseViewModel(
    private val workoutTypeRepository: WorkoutTypeRepository,
    private val exerciseRepository: ExerciseRepository,
    private val sessionStorage: SessionStorage
) : ViewModel(){

    var state by mutableStateOf(ExerciseState())
        private set

    private val eventChannel = Channel<ExerciseEvent>()
    val events = eventChannel.receiveAsFlow()

    //TODO use combine function or it will bug !!
    //Convert to mutablestateof list into StateFlow
    private val shouldFilterExercises =
        snapshotFlow { state.workoutTypes }
        .stateIn(viewModelScope, SharingStarted.Eagerly, state.workoutTypes)

    init {

        viewModelScope.launch {
            val result = workoutTypeRepository.getWorkoutTypes()
            if(result is Result.Success){
                state = state.copy(workoutTypes = result.data.map { it.toWorkoutTypeUI() }.sortedBy { it.name })
            }
        }

        viewModelScope.launch {
            val userId = sessionStorage.get()?.userId ?: ""
            exerciseRepository.getExercises(userId)
                /*.combine(
                    shouldFilterExercises
                ){  exercises, workoutTypes ->
                    exercises.filter { inSelectedWorkoutTypes(it.bodyParts) }
                }*/.onEach { filteredExercises ->
                    Timber.d("Exercise From source")
                    state = state.copy(exercises = filteredExercises.map { it.toExerciseUi() })
                }
        }

        shouldFilterExercises.onEach { workoutTypes ->
            val exercises = state.exercises
            val filteredExercises = exercises.filter { inSelectedWorkoutTypes(it.bodyPart) }
            state = state.copy(exercises = filteredExercises)
            Timber.d("Filtered Exercise Again")
        }.launchIn(viewModelScope)
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

    private fun inSelectedWorkoutTypes(bodyPart : List<BodyPartUi>) : Boolean{
        val listBodyPartUi = state.workoutTypes
            .filter { it.selected }
            .flatMap { it.listBodyPart }

        //All selected - return everything
        if(listBodyPartUi.isEmpty()){
            return true
        //WorkoutTypes selected - return  specific
        }else{
            val exerciseBodyParts = bodyPart
            return listBodyPartUi.containsAll(exerciseBodyParts)
        }
    }
}