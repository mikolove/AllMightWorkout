package com.mikolove.allmightworkout.framework.presentation.main.exerciseinprogress

import androidx.lifecycle.*
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.state.doesMessageAlreadyExistInQueue

import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ExerciseInProgressViewModel
constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(){

/*
    */
/*
        Observable data
    *//*

    val state: MutableLiveData<ExerciseInProgressState> = MutableLiveData(ExerciseInProgressState())

    val chronometerManager = ChronometerManager()
    val chronometerState: LiveData<ChronometerState>
        get() = chronometerManager.getChronometerSate()


    init {
        savedStateHandle.get<String>("idExercise")?.let { idExercise ->
            onTriggerEvent(ExerciseInProgressEvents.GetExerciseById(idExercise = idExercise))
        }
    }

    fun onTriggerEvent(event : ExerciseInProgressEvents){
        when(event){

            is ExerciseInProgressEvents.GetExerciseById->{
                getExerciseById(event.idExercise)
            }
            is ExerciseInProgressEvents.LoadNextSet->{
                loadNextSet()
            }
            is ExerciseInProgressEvents.UpdateActualSet->{
                updateActualSet(event.set)
            }
            is ExerciseInProgressEvents.UpdateExerciseSet->{
                updateExerciseSet(event.set)
            }
            is ExerciseInProgressEvents.LaunchDialog ->{
                appendToMessageQueue(event.message)
            }
            is ExerciseInProgressEvents.Error -> {
                appendToMessageQueue(event.message)
            }
            is ExerciseInProgressEvents.OnRemoveHeadFromQueue ->{
                removeHeadFromQueue()
            }
        }
    }

    */
/*
        Functions
     *//*


    private fun loadNextSet(){
        state.value?.exercise?.let { exercise ->

            state.value?.actualSet?.let { set ->

                val nextOrderSet = set.order.plus(1)

                printLogD("ExerciseInProgressViewModel","nextOrderSet $nextOrderSet")
                exercise.sets.find { it.order == nextOrderSet }?.let {
                    printLogD("ExerciseInProgressViewModel","launch Update Actual Set")
                    onTriggerEvent(ExerciseInProgressEvents.UpdateActualSet(it))
                }
            }
        }
    }

    private fun updateExerciseSet(set : ExerciseSet){
        state.value?.let { state ->
            state.exercise?.let { exercise ->

                val updatedExercise = exercise.copy()

                updatedExercise.sets.find { it.idExerciseSet == set.idExerciseSet}?.apply {
                    startedAt = set.startedAt
                    endedAt = set.endedAt
                }

                this.state.value = state.copy(exercise = updatedExercise)
            }
        }
    }

    private fun updateActualSet(set : ExerciseSet){
        state.value?.let { state ->
            this.state.value = state.copy(actualSet = set)
        }
    }

    */
/*
        Interactors
     *//*


    private fun getExerciseById(idExercise : String){
        state.value?.let { state ->
            inProgressListInteractors.getExerciseById
                .execute(idExercise)
                .onEach { dataState ->

                    dataState?.isLoading?.let { this.state.value = state.copy(isLoading = it) }

                    dataState?.data?.let { exercise ->

                        this.state.value = state.copy(exercise= exercise)

                        exercise.sets.minWithOrNull(compareBy { it.order })?.let { set ->
                            onTriggerEvent(ExerciseInProgressEvents.UpdateActualSet(set))
                        }
                    }

                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
        }
    }

    */
/*
        Queue managing
    *//*


    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            }catch (e: Exception){
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }
*/

}