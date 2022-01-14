package com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InProgressListInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


const val WIP_ARE_YOU_SURE_STOP_EXERCISE = "Are you sure to stop this workout ? You can save it in it's current state or not."
const val WIP_ERROR_LOADING_EXERCISE = "Error loading exercise."
const val WIP_ARE_YOU_SURE_QUIT_NO_SAVE = "Are you sure to quit this workout ? It will not be save."


@HiltViewModel
class WorkoutInProgressViewModel
@Inject
constructor(
    private val inProgressListInteractors: InProgressListInteractors,
    private val savedStateHandle: SavedStateHandle,
    private val dateUtil: DateUtil
    ) : ViewModel() {


    /*
    Observable data
   */
    val state: MutableLiveData<WorkoutInProgressState> = MutableLiveData(WorkoutInProgressState())

    init {
        savedStateHandle.get<String>("idWorkout")?.let { idWorkout ->
            onTriggerEvent(WorkoutInProgressEvents.GetWorkoutById(idWorkout = idWorkout))
        }
    }

    fun onTriggerEvent(event : WorkoutInProgressEvents){
        when(event){

            is WorkoutInProgressEvents.GetWorkoutById->{
                getWorkoutById(event.idWorkout)
            }
            is WorkoutInProgressEvents.InsertHistory->{
                insertHistory()
            }
            is WorkoutInProgressEvents.UpdateExercise->{
                updateExercise(event.exercise)
            }
            is WorkoutInProgressEvents.UpdateWorkoutEnded->{
                updateWorkoutEnded()
            }
            is WorkoutInProgressEvents.UpdateWorkoutDone->{
                updateWorkoutDone()
            }
            is WorkoutInProgressEvents.LaunchDialog ->{
                appendToMessageQueue(event.message)
            }
            is WorkoutInProgressEvents.Error -> {
                appendToMessageQueue(event.message)
            }
            is WorkoutInProgressEvents.OnRemoveHeadFromQueue ->{
                removeHeadFromQueue()
            }
        }
    }

    /*
        Functions
     */

    private fun updateExercise(exercise : Exercise){
        state.value?.let { state ->

            state.workout?.let { workout ->

                val updatedWorkout = workout.copy()

                updatedWorkout.exercises?.find { it.idExercise == exercise.idExercise}?.apply {
                    sets = exercise.sets
                    startedAt = exercise.startedAt
                    endedAt = exercise.endedAt
                }

                this.state.value = state.copy(workout = updatedWorkout)
            }
        }
    }

    private fun updateWorkoutDone(){
        state.value?.let { state ->
            state.workout?.exercises?.let { exercises ->
                if(exercises.all { it.endedAt != null }){
                    this.state.value = state.copy(isWorkoutDone = true)
                }
            }
        }
    }

    private fun updateExitWorkout(){
        state.value?.let { state ->
            this.state.value = state.copy(exitWorkout = true)
        }
    }

    private fun updateWorkoutEnded(){
        state.value?.let { state ->
            state.workout?.let { workout ->
                val updateWorkout = workout.copy(endedAt = dateUtil.getCurrentTimestamp())
                this.state.value = state.copy(workout = updateWorkout)
            }
        }
    }

    /*
        Interactors
     */

    private fun getWorkoutById(idWorkout: String){
        state.value?.let { state ->

            inProgressListInteractors.getWorkoutById
                .execute(idWorkout)
                .onEach { dataState ->

                    dataState?.data?.let { workout ->
                        workout.startedAt = dateUtil.getCurrentTimestamp()
                        this.state.value = state.copy(workout = workout)
                    }

                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun insertHistory(){
        state.value?.let { state ->
            state.workout?.let { workout ->
                inProgressListInteractors.insertHistory
                    .execute(workout)
                    .onEach { dataState ->

                        dataState?.isLoading?.let { this.state.value = state.copy(isLoading = it) }

                        dataState?.data?.let { idHistory ->
                        }

                        dataState?.message?.let {  message ->
                            updateExitWorkout()
                            appendToMessageQueue(message)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }
    /*
    Queue managing
    */

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



    /*

    Workout In Progress
    */

/*
    fun getWorkout() : Workout? = getCurrentViewStateOrNew().workout ?: null
    fun getExerciseList() : List<Exercise> = getCurrentViewStateOrNew().exerciseList ?: ArrayList()

    fun loadNextSet(actualSet : ExerciseSet) : Boolean{

        val nextOrderSet = actualSet.order.plus(1)
        val nextSet = getSets()?.find { it.order == nextOrderSet }

        return nextSet?.let {
            setActualSet(it)
            true
        }?:false
    }


    fun setExerciseList(exercises : List<Exercise>?){
        val update = getCurrentViewStateOrNew()
        update.exerciseList = exercises
        setViewState(update)
    }

    fun setExerciseSetList(sets : List<ExerciseSet>?){
        val update = getCurrentViewStateOrNew()
        update.setList = sets
        setViewState(update)
    }

    fun setWorkout(workout : Workout?){
        val update = getCurrentViewStateOrNew()
        update.workout = workout
        setViewState(update)
    }

    val chronometerManager = ChronometerManager()
    val chronometerState: LiveData<ChronometerState>
        get() = chronometerManager.getChronometerSate()

    fun getExercise() : Exercise? = getCurrentViewStateOrNew().exercise
    fun getSets() : List<ExerciseSet> = getCurrentViewStateOrNew().setList ?: ArrayList()

    fun setActualSet(set : ExerciseSet){
        val update = getCurrentViewStateOrNew()
        update.actualSet = set
        setViewState(update)
    }

    fun getActualSet() = getCurrentViewStateOrNew().actualSet

    fun geTotalSets() = getCurrentViewStateOrNew().setList?.size?.plus(1) ?: 0

    fun setExercise(exercise : Exercise?){
        val update = getCurrentViewStateOrNew()
        update.exercise = exercise
        setViewState(update)
    }


    fun saveSet(set :ExerciseSet){

        val updateExercise = getExercise()

        updateExercise?.let {
            it?.sets?.find { it.idExerciseSet == set.idExerciseSet }?.apply {
                startedAt = set.startedAt
                endedAt = set.endedAt
            }
            setExercise(it)
        }
    }

    fun saveExercise(){
        val updateExercise = getExercise()?.copy(
             endedAt = dateUtil.getCurrentTimestamp()
        )
        val updatedExerciseList = getCurrentViewStateOrNew().exerciseList
        updatedExerciseList?.find { it.idExercise == updateExercise?.idExercise }?.apply {
            startedAt = updateExercise?.startedAt
            endedAt = updateExercise?.endedAt
            sets = updateExercise?.sets ?: ArrayList()
        }

        updatedExerciseList?.let {
            setExerciseList(it)
            var isDone = true
            for(exercise in it){
                if(exercise.startedAt == null || exercise.endedAt == null)
                    isDone = false
            }
            setIsWorkoutDone(isDone)
        }
    }

    fun setIsWorkoutDone(isDone : Boolean?){
        val update = getCurrentViewStateOrNew()
        update.isWorkoutDone = isDone
        setViewState(update)
    }


    fun isWorkoutDone() = getCurrentViewStateOrNew().isWorkoutDone ?: false

    fun saveWorkout(workout : Workout, exercises: List<Exercise>){

        val exercisesDone = exercises.filter { it.endedAt != null }

        val updateWorkout = workout.copy(
            endedAt = dateUtil.getCurrentTimestamp(),
            exercises = exercisesDone
        )
        setStateEvent(InsertHistoryEvent(workout = updateWorkout))
    }
*/

}