package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.common.GetExercises
import com.mikolove.allmightworkout.business.interactors.main.exercise.ExerciseInteractors
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExercises
import com.mikolove.allmightworkout.business.interactors.main.workout.GetWorkouts
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.framework.presentation.common.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutFilterOptions
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutOrderOptions
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val exerciseInteractors: ExerciseInteractors,
    private val appDataStoreManager: AppDataStore,
) : ViewModel() {

    val state: MutableLiveData<ExerciseListState> = MutableLiveData(ExerciseListState())

    val exerciseListInteractionManager = ListInteractionManager<Exercise>()

    val exerciseToolbarState: LiveData<ListToolbarState>
        get() = exerciseListInteractionManager.toolbarState

    init {
        printLogD("ExerciseListViewModel","Launch get and order")
        onTriggerEvent(ExerciseListEvents.GetOrderAndFilter)
    }


    fun onTriggerEvent(event: ExerciseListEvents){
        when(event) {
            is ExerciseListEvents.NewSearch ->{
                search()
            }
            is ExerciseListEvents.NextPage ->{
                nextPage()
            }
            is ExerciseListEvents.Refresh ->{
                getExercises()
            }
            is ExerciseListEvents.UpdateQuery ->{
                onUpdateQuery(event.query)
            }
            is ExerciseListEvents.UpdateFilter ->{
                onUpdateFilter(event.filter)
            }
            is ExerciseListEvents.UpdateOrder ->{
                onUpdateOrder(event.order)
            }
            is ExerciseListEvents.GetOrderAndFilter ->{
                getOrderAndFilter()
            }
            is ExerciseListEvents.RemoveSelectedExercises ->{
                removeSelectedExercises()
            }
            is ExerciseListEvents.LaunchDialog ->{
                appendToMessageQueue(event.message)
            }
            is ExerciseListEvents.Error -> {
                appendToMessageQueue(event.message)
            }
            is ExerciseListEvents.OnRemoveHeadFromQueue ->{
                removeHeadFromQueue()
            }
        }

    }


    /*
      Functions
   */
    private fun search(){
        resetPage()
        onUpdateQueryExhausted(false)
        getExercises()
    }

    private fun nextPage(){
        incrementPageNumber()
        getExercises()
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

    private fun onUpdateFilter(filter : ExerciseFilterOptions){
        state.value?.let { state ->
            this.state.value = state.copy(list_filter = filter)
            saveFilterOptions(filter.value, state.list_order.value)
        }
    }

    private fun onUpdateOrder(order : ExerciseOrderOptions){
        state.value?.let { state ->
            this.state.value = state.copy(list_order = order)
            saveFilterOptions(state.list_filter.value,order.value)
        }
    }

    private fun saveFilterOptions(filter: String, order: String) {
        viewModelScope.launch {
            appDataStoreManager.setValue(DataStoreKeys.EXERCISE_FILTER, filter)
            appDataStoreManager.setValue(DataStoreKeys.EXERCISE_ORDER, order)
        }
    }

    /*
    Getters & Setters
 */

    fun getSearchQuery(): String {
        return state.value?.query ?: return ""
    }

    fun getSelectedExercises() = exerciseListInteractionManager.getSelectedItems()

    fun setExerciseToolbarState(state: ListToolbarState)
            = exerciseListInteractionManager.setToolbarState(state)

    fun isExerciseMultiSelectionStateActive()
            = exerciseListInteractionManager.isMultiSelectionStateActive()

    fun addOrRemoveExerciseFromSelectedList(exercise: Exercise)
            = exerciseListInteractionManager.addOrRemoveItemFromSelectedList(exercise)

    fun isExerciseSelected(exercise: Exercise): Boolean
            = exerciseListInteractionManager.isItemSelected(exercise)

    fun clearSelectedExercises() = exerciseListInteractionManager.clearSelectedItems()

    fun setIsSearchActive(isActive : Boolean){
        state.value?.let { state ->
            this.state.value = state.copy(searchActive = isActive)
        }
    }

    fun isSearchActive() : Boolean{
        return state.value?.searchActive ?: return false
    }

    /*
       Interactors
    */

    private fun getOrderAndFilter(){
        state.value?.let { state ->
            exerciseInteractors.getExerciseOrderAndFilter.execute()
                .onEach { dataState ->

                    dataState.data?.let { orderAndFilter ->
                        val order = orderAndFilter.order
                        val filter = orderAndFilter.filter
                        this.state.value = state.copy(
                            list_order = order,
                            list_filter = filter
                        )
                        onTriggerEvent(ExerciseListEvents.NewSearch)
                    }

                    dataState.message?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun getExercises(){
        state.value?.let { state ->

            sessionManager.state.value?.idUser?.let { idUser ->

                exerciseInteractors.getExercises.execute(
                    idUser = idUser,
                    query = state.query,
                    filterAndOrder = state.list_order.value + state.list_filter.value,
                    page = state.page
                ).onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState?.isLoading)

                    dataState?.data?.let { listExercise ->
                        this.state.value = state.copy(listExercises = listExercise)
                    }

                    dataState?.message?.let { message ->

                        if(message.description.equals(GetExercises.GET_EXERCISES_SUCCESS_END)){
                            onUpdateQueryExhausted(true)
                        }

                        appendToMessageQueue(message)
                    }

                }.launchIn(viewModelScope)
            }
        }
    }


    private fun removeSelectedExercises(){
        state.value?.let { state ->
            exerciseInteractors.removeMultipleExercises.execute(
                exercises = getSelectedExercises()
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.message?.let { message ->

                    setExerciseToolbarState(ListToolbarState.SelectionState())
                    clearSelectedExercises()

                    appendToMessageQueue(message)

                    if(message.description.equals(RemoveMultipleExercises.DELETE_EXERCISES_SUCCESS)){
                        onTriggerEvent(ExerciseListEvents.NewSearch)
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    /*
        Queue managing
     */

    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                printLogD("ExerciseListViewModel","peek item ${queue.peek()?.id}")
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
                printLogD("ExerciseListViewModel","Removed from queue")
            }catch (e: Exception){
                printLogD("ExerciseListViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    printLogD("ExerciseListViewModel","Added to queue message")
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

}
