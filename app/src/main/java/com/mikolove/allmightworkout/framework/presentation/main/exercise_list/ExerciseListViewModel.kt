package com.mikolove.allmightworkout.framework.presentation.main.exercise_list

import androidx.lifecycle.ViewModel
import com.mikolove.core.data.datastore.AppDataStore

/*class ExerciseListViewModel
constructor(
    private val sessionManager: SessionManager,

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


    */
/*
      Functions
   *//*

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

    */
/*
    Getters & Setters
 *//*


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

    */
/*
       Interactors
    *//*


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

            sessionManager.getUserId()?.let { idUser ->

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

                        if(message.description.equals(com.mikolove.core.interactors.common.GetExercises.GET_EXERCISES_SUCCESS_END)){
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

                    if(message.description.equals(com.mikolove.core.interactors.exercise.RemoveMultipleExercises.DELETE_EXERCISES_SUCCESS)){
                        onTriggerEvent(ExerciseListEvents.NewSearch)
                    }
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


}*/
