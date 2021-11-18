package com.mikolove.allmightworkout.framework.presentation.main.workout_detail

import androidx.lifecycle.*
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutInteractors
import com.mikolove.allmightworkout.util.printLogD

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel
@Inject
constructor(
    private val workoutInteractors : WorkoutInteractors,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(){

    val state: MutableLiveData<WorkoutDetailState> = MutableLiveData(WorkoutDetailState())

    init {
        savedStateHandle.get<String>("idWorkout")?.let { idWorkout ->
            printLogD("WorkoutDetailViewModel","Launch Get workout by id ${idWorkout}")
            onTriggerEvent(WorkoutDetailEvents.GetWorkoutById(idWorkout = idWorkout))
        }
    }

    private val workoutInteractionManager: WorkoutInteractionManager = WorkoutInteractionManager()

    val workoutNameInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutNameState

    val workoutIsActiveInteractionState: LiveData<WorkoutInteractionState>
        get() = workoutInteractionManager.workoutIsActiveState


    fun onTriggerEvent(event: WorkoutDetailEvents){
        when(event){
            is WorkoutDetailEvents.GetWorkoutById->{
                getWorkoutById(event.idWorkout)
            }
            is WorkoutDetailEvents.UpdateWorkout->{
                updateWorkout()
            }
            is WorkoutDetailEvents.RemoveWorkout->{
                removeWorkout()
            }
            is WorkoutDetailEvents.OnUpdateWorkout->{
                onUpdateWorkout(event.name,event.isActive)
            }
            is WorkoutDetailEvents.OnUpdateIsPending ->{
                onUpdateIsPending(event.isPending)
            }
            is WorkoutDetailEvents.Error->{

            }
            is WorkoutDetailEvents.LaunchDialog->{
                appendToMessageQueue(event.message)
            }
            is WorkoutDetailEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }


    private fun onUpdateIsPending(isPending : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(isUpdatePending = isPending)
        }
    }

    private fun onUpdateWorkout(name : String, isActive : Boolean){
        state.value?.let { state ->
            val updatedWorkout = state.workout?.copy(name = name, isActive = isActive)
            this.state.value = state.copy(workout = updatedWorkout)
        }
    }

    /********************************************************************
    INTERACTORS
     *********************************************************************/

    private fun getWorkoutById(idWorkout : String){
        state.value?.let { state ->

            workoutInteractors.getWorkoutById.execute(
                idWorkout = idWorkout
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.data?.let { workout ->
                    this.state.value = state.copy(workout = workout)
                }

                dataState?.message?.let { message ->
                    appendToMessageQueue(message)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateWorkout(){
        state.value?.let { state ->
            state.workout?.let { workout ->
                workoutInteractors.updateWorkout.execute(
                    workout = workout
                ).onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState?.isLoading)

                    dataState?.data?.let {
                        onTriggerEvent(WorkoutDetailEvents.OnUpdateIsPending(false))
                    }

                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun removeWorkout(){
        state.value?.let { state ->
            state.workout?.let { workout ->
                workoutInteractors.removeWorkout.execute(
                    workout = workout
                ).onEach { dataState ->
                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
    /********************************************************************
    INTERACTIONS
     *********************************************************************/


    fun setWorkoutInteractionNameState(state : WorkoutInteractionState){
        workoutInteractionManager.setWorkoutNameState(state)
    }

    fun setWorkoutInteractionIsActiveState(state : WorkoutInteractionState){
        workoutInteractionManager.setWorkoutIsActiveState(state)
    }

    fun checkEditState() = workoutInteractionManager.checkEditState()

    fun exitEditState() = workoutInteractionManager.exitEditState()

    fun isEditingName() = workoutInteractionManager.isEditingName()

    fun isEditingIsActive() = workoutInteractionManager.isEditingIsActive()

    /********************************************************************
    QUEUE MANAGING
     *********************************************************************/

    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                printLogD("WorkoutListViewModel","peek item ${queue.peek()?.id}")
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
                printLogD("WorkoutListViewModel","Removed from queue")
            }catch (e: Exception){
                printLogD("WorkoutListViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    printLogD("WorkoutListViewModel","Added to queue message")
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }
}