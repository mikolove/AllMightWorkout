package com.mikolove.allmightworkout.framework.presentation.main.exercise_set_detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mikolove.allmightworkout.util.printLogD
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.state.doesMessageAlreadyExistInQueue


class ExerciseSetDetailViewModel
constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(){
/*


    val state : MutableLiveData<ExerciseSetDetailState> = MutableLiveData(ExerciseSetDetailState())

    init {

        val exerciseType = savedStateHandle.get<ExerciseType>("exerciseType")
        val set = savedStateHandle.get<ExerciseSet>("exerciseSet")
        if(exerciseType != null && set !=null){
            this.state.value = state.value?.copy(set = set,exerciseType = exerciseType)
            onTriggerEvent(ExerciseSetDetailEvents.UpdateLoadInitialValues(true))
        }
    }

    fun onTriggerEvent(event : ExerciseSetDetailEvents) {
        when(event){
            is ExerciseSetDetailEvents.UpdateLoadInitialValues->{
                updateLoadInitialValues(event.load)
            }
            is ExerciseSetDetailEvents.UpdateReps->{
                updateReps(event.reps)
            }
            is ExerciseSetDetailEvents.UpdateTime->{
                updateTime(event.time)
            }
            is ExerciseSetDetailEvents.UpdateRestTime->{
                updateRestTime(event.restTime)
            }
            is ExerciseSetDetailEvents.UpdateWeight->{
                updateWeight(event.weight)
            }
            is ExerciseSetDetailEvents.OnUpdateIsPending->{
                onUpdateIsPending(event.isPending)
            }
            is ExerciseSetDetailEvents.Error->{

            }
            is ExerciseSetDetailEvents.LaunchDialog->{
                appendToMessageQueue(event.message)
            }
            is ExerciseSetDetailEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    */
/********************************************************************
    Functions
     *********************************************************************//*


    private fun updateLoadInitialValues(load : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(loadInitialValues = load)
        }
    }

    private fun onUpdateIsPending(isPending : Boolean){
        state.value?.let { state ->
            val updatedSet = state.set?.copy(updatedAt = dateUtil.getCurrentTimestamp())
            this.state.value = state.copy(set = updatedSet, isUpdatePending = isPending)
        }
    }
    private fun updateReps(reps : Int){
        state.value?.let { state->
            val updatedSet = state.set?.copy(reps = reps)
            this.state.value = state.copy(set = updatedSet)
        }
    }

    private fun updateTime(time : Int){
        state.value?.let { state->
            val updatedSet = state.set?.copy(time = time)
            this.state.value = state.copy(set = updatedSet)
        }
    }

    private fun updateRestTime(restTime : Int){
        state.value?.let { state->
            val updatedSet = state.set?.copy(restTime = restTime)
            this.state.value = state.copy(set = updatedSet)
        }
    }

    private fun updateWeight(weight : Int){
        state.value?.let { state->
            val updatedSet = state.set?.copy(weight = weight)
            this.state.value = state.copy(set = updatedSet)
        }
    }
    */
/********************************************************************
    Live Data
     *********************************************************************//*


    private val exerciseSetInteractionManager: ExerciseSetInteractionManager = ExerciseSetInteractionManager()

    val repInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.repState

    val weightInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.weightState

    val timeInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.timeState

    val restInteractionState : LiveData<ExerciseSetInteractionState>
        get() = exerciseSetInteractionManager.restState

    fun setInteractionRepState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setRepState(state)
    }

    fun setInteractionWeightState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setWeightState(state)
    }

    fun setInteractionTimeState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setTimeState(state)
    }

    fun setInteractionRestState(state : ExerciseSetInteractionState){
        exerciseSetInteractionManager.setRestState(state)
    }

    fun isEditingRep() = exerciseSetInteractionManager.isEditingRep()

    fun isEditingWeight() = exerciseSetInteractionManager.isEditingWeight()

    fun isEditingRest() = exerciseSetInteractionManager.isEditingRest()

    fun isEditingTime() = exerciseSetInteractionManager.isEditingTime()

    fun exitSetEditState() = exerciseSetInteractionManager.exitEditState()

    fun checkSetEditState() = exerciseSetInteractionManager.checkEditState()


    */
/********************************************************************
    QUEUE MANAGING
     *********************************************************************//*


    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            }catch (e: Exception){
                printLogD("ExerciseSetDetailViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    printLogD("ExerciseSetDetailViewModel","Added to queue message")
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }
*/

}