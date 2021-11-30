package com.mikolove.allmightworkout.framework.presentation.main.workout_add_exercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.common.GetExercises.Companion.GET_EXERCISES_SUCCESS_END
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutInteractors
import com.mikolove.allmightworkout.framework.presentation.main.exercise.ExerciseFilterOptions
import com.mikolove.allmightworkout.framework.presentation.main.exercise.ExerciseOrderOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WorkoutExerciseViewModel
@Inject
constructor(
    private val workoutInteractors: WorkoutInteractors,
    private val exerciseInteractors: ExerciseInteractors,
    private val savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    val state: MutableLiveData<WorkoutExerciseState> = MutableLiveData(WorkoutExerciseState())


    init {
        savedStateHandle.get<String>("idWorkout")?.let { idWorkout ->
            onTriggerEvent(WorkoutExerciseEvents.GetWorkoutById(idWorkout = idWorkout))
        }
    }

    fun onTriggerEvent(event: WorkoutExerciseEvents){
        when(event) {

            is WorkoutExerciseEvents.GetWorkoutById->{
                getWorkoutById(event.idWorkout)
            }
            is WorkoutExerciseEvents.NewSearch->{
                search()
            }
            is WorkoutExerciseEvents.ReloadList->{
                getWorkoutExercises()
            }
            is WorkoutExerciseEvents.NextPage->{
                nextPage()
            }
            is WorkoutExerciseEvents.UpdateQuery->{
                onUpdateQuery(event.query)
            }
            is WorkoutExerciseEvents.AddExerciseToWorkout->{
                addExerciseToWorkout(event.idExercise)
            }
            is WorkoutExerciseEvents.RemoveExerciseFromWorkout->{
                removeExerciseFromWorkout(event.idExercise)
            }
            is WorkoutExerciseEvents.LaunchDialog->{
                appendToMessageQueue(event.message)
            }
            is WorkoutExerciseEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    /*
    Functions
    */
    fun search(){
        resetPage()
        onUpdateQueryExhausted(false)
        getWorkoutExercises()
    }

    fun nextPage(){
        incrementPageNumber()
        getWorkoutExercises()
    }

    fun resetPage(){
        state.value?.let { state ->
            this.state.value = state.copy(page = 1)
        }
    }
    fun incrementPageNumber(){
        state.value?.let { state ->
            this.state.value = state.copy(page = state.page +1)
        }
    }

    private fun onUpdateQuery(query: String) {
        state.value = state.value?.copy(query = query)
    }

    private fun onUpdateQueryExhausted(isExhausted: Boolean) {
        state.value?.let { state ->
            this.state.value = state.copy(isQueryExhausted = isExhausted)
        }
    }

    fun isExerciseInWorkout(exercise: Exercise) :Boolean{
        return state.value?.let {  state ->
            state.workout?.exercises?.contains(exercise)
        } ?: false
    }

    fun setIsSearchActive(isActive : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(searchActive = isActive)
        }
    }

    fun isSearchActive() : Boolean{
        return state.value?.searchActive ?: return false
    }
    fun getSearchQuery(): String {
        return state.value?.query ?: return ""
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
                    onTriggerEvent(WorkoutExerciseEvents.ReloadList)
                }

                dataState?.message?.let { message ->
                    appendToMessageQueue(message)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getWorkoutExercises(){
        state.value?.let { state ->

            exerciseInteractors.getExercises.execute(
                query = state.query,
                filterAndOrder = ExerciseOrderOptions.ASC.value + ExerciseFilterOptions.FILTER_NAME.value,
                page = state.page
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.data?.let { listExercise ->

                    val listWorkoutExercise = listExercise.map { exercise ->
                        WorkoutExercise(
                            selected = isExerciseInWorkout(exercise),
                            exercise = exercise
                        )
                    }.sortedBy { it.exercise.name }

                    this.state.value = state.copy(listWorkoutExercises = listWorkoutExercise)
                }

                dataState?.message?.let { message ->

                    if(message.description.equals(GET_EXERCISES_SUCCESS_END)){
                        onUpdateQueryExhausted(true)
                    }

                    appendToMessageQueue(message)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun addExerciseToWorkout(idExercise :String){
        state.value?.let { state ->
            state.workout?.let { workout ->

                workoutInteractors.addExerciseToWorkout.execute(
                    idWorkout = workout.idWorkout,
                    idExercise = idExercise
                ).onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState?.isLoading)

                    dataState?.data?.let {
                        onTriggerEvent(WorkoutExerciseEvents.GetWorkoutById(workout.idWorkout))
                    }

                    dataState?.message?.let { message ->
                       appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun removeExerciseFromWorkout(idExercise :String){
        state.value?.let { state ->
            state.workout?.let { workout ->

                workoutInteractors.removeExerciseFromWorkout.execute(
                    idWorkout = workout.idWorkout,
                    idExercise = idExercise
                ).onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState?.isLoading)

                    dataState?.data?.let {
                        onTriggerEvent(WorkoutExerciseEvents.GetWorkoutById(workout.idWorkout))
                    }

                    dataState?.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    /********************************************************************
    QUEUE MANAGING
     *********************************************************************/

    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            }catch (e: Exception){ }
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

}