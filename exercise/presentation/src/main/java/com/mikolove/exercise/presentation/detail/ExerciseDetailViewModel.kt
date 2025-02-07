package com.mikolove.exercise.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ExerciseDetailViewModel(
    private val workoutTypeRepository: WorkoutTypeRepository,
    private val offlineFirstExerciseRepository: ExerciseRepository,
    private val sessionStorage: SessionStorage
) : ViewModel(){

    var state by mutableStateOf(ExerciseDetailState())
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
                state.exerciseId?.let{
                    viewModelScope.launch {
                        val cacheResult = offlineFirstExerciseRepository.getExercise(it)
                        if(cacheResult !is Result.Success){
                            //Error loading return to previous page
                        }else{
                            //Initialize fields
                            val name = cacheResult.data.name
                            val bodyPartsSelected = cacheResult.data.bodyParts.map { it.toBodyPartUi() }
                            val workoutTypeSelected = state.workoutTypes.find { it.listBodyPart.containsAll(bodyPartsSelected) }

                            workoutTypeSelected?.let {
                                state.nameSelected.edit { append(name) }
                                state.workoutTypeSelected.edit {
                                    append(it.name)
                                }
                                val bodyPartsUi = it.listBodyPart.map {  bp ->
                                    val isSelected = bodyPartsSelected.any { it.idBodyPart == bp.idBodyPart }
                                    bp.copy(selected = isSelected)
                                }
                                state = state.copy(
                                    bodyParts = bodyPartsUi,
                                    bodyPartsSelected =  bodyPartsUi.filter { it.selected }.map { it.toBodyPart() }.toMutableStateList(),
                                    workoutTypes = resultWorkoutTypes,
                                    exerciseTypes = resultExercisesTypes,
                                    isActiveSelected = cacheResult.data.isActive,
                                    isDataLoaded = true
                                )
                            }
                        }
                    }
                } ?: run {
                    state = state.copy(
                        workoutTypes = resultWorkoutTypes,
                        exerciseTypes = resultExercisesTypes,
                        isActiveSelected = true,
                        isDataLoaded = true)
                }

            }
        }



        //Listen to TextFieldState
        snapshotFlow { state.workoutTypeSelected.text }
            .map { workoutType ->
                if(state.isDataLoaded){
                    val workoutTypeSelected = state.workoutTypes.find { it.name.equals(workoutType.toString(),true)}
                    val bodyPartsUi = workoutTypeSelected?.listBodyPart ?: emptyList()
                    state = state.copy(
                        bodyParts =bodyPartsUi,
                        bodyPartsSelected = mutableStateListOf()
                    )
                }
            }
            .launchIn(viewModelScope)

        //Validator
        combine(
            snapshotFlow { state.nameSelected.text },
            snapshotFlow { state.bodyPartsSelected.toList()},
            snapshotFlow { state.workoutTypeSelected.text},
            snapshotFlow { state.exerciseTypeSelected.text }){
                                                             name, bodyParts,workoutType, exerciseType ->
            if(state.isDataLoaded){
                state = if(name.isNotBlank() && bodyParts.isNotEmpty() && exerciseType.isNotEmpty() && workoutType.isNotEmpty()){
                    state.copy(isExerciseValid = true)
                }else{
                    state.copy(isExerciseValid = false)
                }
                Timber.d("State of Exercise update $name $bodyParts $exerciseType Valid : ${state.isExerciseValid}")
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

        state.exerciseId?.let { exercise.idExercise = it }

        viewModelScope.launch {
            val userId = sessionStorage.get()?.userId ?: ""
            when(val result = offlineFirstExerciseRepository.upsertExercise(exercise, userId)){
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