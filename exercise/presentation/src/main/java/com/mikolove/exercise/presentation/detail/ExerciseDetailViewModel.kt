package com.mikolove.exercise.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workouttype.WorkoutTypeRepository
import com.mikolove.core.presentation.ui.asUiText
import com.mikolove.core.presentation.ui.mapper.toBodyPart
import com.mikolove.core.presentation.ui.mapper.toBodyPartUi
import com.mikolove.core.presentation.ui.mapper.toExerciseTypeUi
import com.mikolove.core.presentation.ui.mapper.toWorkoutTypeUI
import com.mikolove.exercise.domain.ExerciseRepository
import com.mikolove.exercise.presentation.navigation.ExerciseDetailRoute
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    val savedStateHandle: SavedStateHandle,
    private val workoutTypeRepository: WorkoutTypeRepository,
    private val offlineFirstExerciseRepository: ExerciseRepository,
    private val sessionStorage: SessionStorage
) : ViewModel(){


    var state by mutableStateOf(ExerciseDetailState(exerciseId = savedStateHandle.toRoute<ExerciseDetailRoute>().id))
        private set

    private val eventChannel = Channel<ExerciseDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        viewModelScope.launch {

            //Load data
            val workoutTypesAndBodyParts = async{
                val result = workoutTypeRepository.getWorkoutTypes()
                if (result is Result.Success){
                    result.data.map { it.toWorkoutTypeUI() }.sortedBy { it.name }
                }else{
                    emptyList()
                }
            }

            val exercisesTypes = async{
                ExerciseType.entries.map { it.toExerciseTypeUi() }
            }

            val resultWorkoutTypes = workoutTypesAndBodyParts.await()
            val resultExercisesTypes = exercisesTypes.await()

            if (resultWorkoutTypes.isNotEmpty() &&
                resultExercisesTypes.isNotEmpty()
            ) {
                //Load exercise if exist
                if(state.exerciseId.isNotEmpty()){
                    viewModelScope.launch {
                        val cacheResult = offlineFirstExerciseRepository.getExercise(state.exerciseId)
                        when(cacheResult){
                            is Result.Error -> {
                                eventChannel.send(ExerciseDetailEvent.Error(cacheResult.error.asUiText()))
                            }
                            is Result.Success -> {

                                val exercise = cacheResult.data

                                //Initialize fields
                                val bodyPartsSelected = exercise.bodyParts.map { it.toBodyPartUi() }
                                val workoutTypeSelected = resultWorkoutTypes.find { it.listBodyPart.containsAll(bodyPartsSelected) }

                                workoutTypeSelected?.let {

                                    //TextField
                                    state.nameSelected.edit { append(exercise.name) }
                                    state.exerciseTypeSelected.edit { append(exercise.exerciseType.type) }
                                    state.workoutTypeSelected.edit { append(it.name) }

                                    //State
                                    val bodyPartsUi = it.listBodyPart.map {  bp ->
                                        if(bp in bodyPartsSelected) {
                                            bp.copy(selected = true)
                                        }else{
                                            bp
                                        }
                                    }
                                    val workoutTypesUi = resultWorkoutTypes.map { wk ->
                                        if(wk.idWorkoutType == workoutTypeSelected.idWorkoutType){
                                            wk.copy(selected = true)
                                        }else{
                                            wk
                                        } }

                                    state = state.copy(
                                        bodyParts = bodyPartsUi,
                                        workoutTypes = workoutTypesUi,
                                        bodyPartsSelected =  bodyPartsUi.filter { it.selected }.map { it.toBodyPart() }.toMutableStateList(),
                                        exerciseTypes = resultExercisesTypes,
                                        isActiveSelected = exercise.isActive,
                                        isDataLoaded = true
                                    )
                                }
                            }
                        }
                    }
                } else {
                    state = state.copy(
                        workoutTypes = resultWorkoutTypes,
                        exerciseTypes = resultExercisesTypes,
                        isActiveSelected = true,
                        isDataLoaded = true)
                }

            }
        }

        snapshotFlow { state.workoutTypeSelected.text }
            .map { workoutType ->
                if(state.isDataLoaded){
                    val workoutTypeSelected = state.workoutTypes.find { it.name.equals(workoutType.toString(),true)}
                    workoutTypeSelected?.let { wts ->
                        if(!wts.selected){
                            val workoutTypesUi = state.workoutTypes.map {
                                if(it.idWorkoutType == wts.idWorkoutType){
                                    wts.copy(selected = true)
                                }else{
                                    it
                                }
                            }
                            val bodyPartsUi = wts.listBodyPart
                            state = state.copy(
                                workoutTypes = workoutTypesUi,
                                bodyParts =bodyPartsUi,
                                bodyPartsSelected = mutableStateListOf()
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)

        //Validator
        combine(
            snapshotFlow { state.nameSelected.text },
            snapshotFlow { state.bodyPartsSelected.toList()},
            snapshotFlow { state.workoutTypeSelected.text},
            snapshotFlow { state.exerciseTypeSelected.text }){ name, bodyParts,workoutType, exerciseType ->
            if(state.isDataLoaded){
                state = if(name.isNotBlank() && bodyParts.isNotEmpty() && exerciseType.isNotEmpty() && workoutType.isNotEmpty()){
                    state.copy(isExerciseValid = true)
                }else{
                    state.copy(isExerciseValid = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ExerciseDetailAction){
        when(action){
            is ExerciseDetailAction.onBodyPartClick ->{
                switchChipState(action.chipId)
            }

            is ExerciseDetailAction.onUpsertClick -> {
                upsertExercise()
            }

            is ExerciseDetailAction.onDeleteClick -> {
                viewModelScope.launch {
                    offlineFirstExerciseRepository.deleteExercise(action.exerciseId)
                    eventChannel.send(ExerciseDetailEvent.ExerciseDeleted)
                }
            }
            else -> Unit
        }
    }

    private fun switchChipState(chipId : String?) {

        val updatedBodyParts = state.bodyParts.map { bodyPartUi ->
            if(chipId.isNullOrEmpty()){
                bodyPartUi.copy(selected = false)
            }else{
                if(bodyPartUi.idBodyPart == chipId){
                    bodyPartUi.copy(selected = !bodyPartUi.selected)
                }else{
                    bodyPartUi
                }
            }
        }

        state = state.copy(
            bodyParts = updatedBodyParts,
            bodyPartsSelected = updatedBodyParts.filter { it.selected }.map { it.toBodyPart() }.toMutableStateList()
        )
    }

    private fun upsertExercise(){

        val exercise = ExerciseFactory()
            .createExercise(
                name = state.nameSelected.text.toString().trim(),
                bodyParts = state.bodyPartsSelected.toList(),
                exerciseType = ExerciseType.from(state.exerciseTypeSelected.text.toString()),
                isActive = state.isActiveSelected
            )

        if(state.exerciseId.isNotBlank()){
            exercise.idExercise = state.exerciseId
        }

        viewModelScope.launch {
            when(val result = offlineFirstExerciseRepository.upsertExercise(exercise)){
                is Result.Error -> {
                    eventChannel.send(ExerciseDetailEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(ExerciseDetailEvent.ExerciseSaved)
                }
            }
        }
    }
}