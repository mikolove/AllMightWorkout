package com.mikolove.allmightworkout.framework.presentation.main.workout_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.*
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertWorkout.Companion.INSERT_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveMultipleWorkouts.Companion.DELETE_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys.Companion.WORKOUT_FILTER
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys.Companion.WORKOUT_ORDER
import com.mikolove.allmightworkout.framework.presentation.common.ListInteractionManager
import com.mikolove.allmightworkout.framework.presentation.common.ListToolbarState
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutListEvents.InsertWorkout
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutListViewModel
@Inject
constructor(
    private val workoutInteractors: WorkoutInteractors,
    private val workoutFactory: WorkoutFactory,
    private val dateUtil: DateUtil,
    private val appDataStoreManager: AppDataStore,

    ) : ViewModel() {
    
    /*
        Observable data
     */
    val state: MutableLiveData<WorkoutListState> = MutableLiveData(WorkoutListState())

    val workoutListInteractionManager = ListInteractionManager<Workout>()

    val workoutToolbarState: LiveData<ListToolbarState>
        get() = workoutListInteractionManager.toolbarState

    init {
        onTriggerEvent(GetOrderAndFilter)
    }

    /*
        Event
     */
    fun onTriggerEvent(event: WorkoutListEvents){
        when(event) {
            is NewSearch ->{
                search()
            }
            is NextPage->{
                nextPage()
            }
            is UpdateQuery->{
                onUpdateQuery(event.query)
            }
            is UpdateFilter->{
                onUpdateFilter(event.filter)
            }
            is UpdateOrder->{
                onUpdateOrder(event.order)
            }
            is GetOrderAndFilter->{
                getOrderAndFilter()
            }
            is InsertWorkout->{
                insertWorkout(event.name)
            }
            is RemoveSelectedWorkouts->{
                removeSelectedWorkouts()
            }
            is LaunchDialog->{
                appendToMessageQueue(event.message)
            }
            is Error -> {
                appendToMessageQueue(event.message)
            }
            is OnRemoveHeadFromQueue ->{
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
        getWorkouts()
    }

    fun nextPage(){
        incrementPageNumber()
        getWorkouts()
    }

    fun clearList(){
        state.value?.let { state ->
            this.state.value = state.copy(listWorkouts = listOf())
        }
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

    private fun onUpdateFilter(filter : WorkoutFilterOptions){
        state.value?.let { state ->
            this.state.value = state.copy(list_filter = filter)
            saveFilterOptions(filter.value, state.list_order.value)
        }
    }

    private fun onUpdateOrder(order : WorkoutOrderOptions){
        state.value?.let { state ->
            this.state.value = state.copy(list_order = order)
            saveFilterOptions(state.list_filter.value,order.value)
        }
    }

    private fun saveFilterOptions(filter: String, order: String) {
        viewModelScope.launch {
            appDataStoreManager.setValue(WORKOUT_FILTER, filter)
            appDataStoreManager.setValue(WORKOUT_ORDER, order)
        }
    }

    /*
        Getters & Setters
     */

    fun getSearchQuery(): String {
        return state.value?.query ?: return ""
    }

    private fun getPageWorkouts(): Int{
        return state.value?.page ?: return 1
    }

    fun getSelectedWorkouts() = workoutListInteractionManager.getSelectedItems()

    fun setWorkoutToolbarState(state: ListToolbarState)
            = workoutListInteractionManager.setToolbarState(state)

    fun isWorkoutMultiSelectionStateActive()
            = workoutListInteractionManager.isMultiSelectionStateActive()

    fun addOrRemoveWorkoutFromSelectedList(workout: Workout)
            = workoutListInteractionManager.addOrRemoveItemFromSelectedList(workout)

    fun isWorkoutSelected(workout: Workout): Boolean
            = workoutListInteractionManager.isItemSelected(workout)

    fun clearSelectedWorkouts() = workoutListInteractionManager.clearSelectedItems()

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
            workoutInteractors.getWorkoutOrderAndFilter.execute()
                .onEach { dataState ->

                    dataState.data?.let { orderAndFilter ->
                        val order = orderAndFilter.order
                        val filter = orderAndFilter.filter
                        this.state.value = state.copy(
                            list_order = order,
                            list_filter = filter
                        )
                        onTriggerEvent(NewSearch)
                    }

                    dataState.message?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun getWorkouts(){
        state.value?.let { state ->

            workoutInteractors.getWorkouts.execute(
                query = state.query,
                filterAndOrder = state.list_order.value + state.list_filter.value,
                page = state.page
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = dataState?.isLoading)

                dataState?.data?.let { listWorkout ->
                    this.state.value = state.copy(listWorkouts = listWorkout)
                }

                dataState?.message?.let { message ->

                    if(message.description.equals(GetWorkouts.GET_WORKOUTS_SUCCESS_END)){
                        onUpdateQueryExhausted(true)
                    }

                    appendToMessageQueue(message)
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun insertWorkout(name : String){
        state.value?.let { state ->
            workoutInteractors.insertWorkout.execute(
                name = name
            ).onEach { dataState ->

                dataState?.data?.let{
                    this.state.value = state.copy(insertedWorkout = it)
                }

                dataState?.message?.let { message ->
                    appendToMessageQueue(message)
                    if(message.description.equals(INSERT_WORKOUT_SUCCESS)){
                        search()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun removeSelectedWorkouts(){
        state.value?.let { state ->
            workoutInteractors.removeMultipleWorkouts.execute(
                workouts = getSelectedWorkouts()
            ).onEach { dataState ->

                this.state.value = state.copy(isLoading = state.isLoading)

                dataState?.message?.let { message ->

                    setWorkoutToolbarState(ListToolbarState.SelectionState())
                    clearSelectedWorkouts()

                    appendToMessageQueue(message)

                    if(message.description.equals(DELETE_WORKOUTS_SUCCESS)){
                        onTriggerEvent(NewSearch)
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