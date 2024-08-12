package com.mikolove.allmightworkout.framework.presentation.main.history

import androidx.lifecycle.ViewModel
import com.mikolove.core.interactors.analytics.HistoryListInteractors

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    private val historyListInteractors: com.mikolove.core.interactors.analytics.HistoryListInteractors
) : ViewModel() { /*: BaseViewModel<HistoryViewState>(){

    override fun initNewViewState(): HistoryViewState {
        return HistoryViewState()
    }
    override fun handleNewData(data: HistoryViewState) {
        data.let { viewState ->

            viewState.listHistoryWorkouts?.let { history ->
                setListHistory(history)
            }

            viewState.totalHistoryWorkout?.let { total ->
                setTotalHistory(total)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job : Flow<DataState<HistoryViewState>?> = when(stateEvent) {

            is HistoryStateEvent.GetHistoryWorkoutsEvent -> {
                historyListInteractors.getHistoryWorkouts.getHistoryWorkouts(
                    query = getSearchQueryHistory(),
                    filterAndOrder = getOrderHistory() + getFilterHistory(),
                    page = getPageHistory(),
                    stateEvent = stateEvent
                )
            }

            is HistoryStateEvent.GetTotalHistoryWorkoutsEvent -> {
                historyListInteractors.getTotalHistoryWorkouts.getTotalWorkouts(stateEvent)
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent,job)
    }

    *//********************************************************************
    GETTERS - VIEWSTATE AND OTHER
     *********************************************************************//*

    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun getHistory() = getCurrentViewStateOrNew().listHistoryWorkouts

    fun getTotalHistory()  = getCurrentViewStateOrNew().totalHistoryWorkout ?: 0

    fun getHistoryListSize() = getCurrentViewStateOrNew().listHistoryWorkouts?.size?: 0

    fun isHistoryPaginationExhausted() = getHistoryListSize() >= getTotalHistory()

    fun getOrderHistory(): String {
        return HISTORY_WORKOUT_ORDER_BY_DESC_DATE_CREATED
    }

    fun getFilterHistory(): String {
        return  HISTORY_WORKOUT_FILTER_NAME
    }

    fun getSearchQueryHistory() : String{
        return ""
    }
    private fun getPageHistory(): Int{
        return getCurrentViewStateOrNew().page ?: return 1
    }

    fun startNewSearch(){
        setQueryExhausted(false)
        resetPage()
        loadHistory()
    }


    fun clearListHistory() {
        val update = getCurrentViewStateOrNew()
        update.listHistoryWorkouts = ArrayList()
        setViewState(update)
    }

    private fun setListHistory(history : ArrayList<HistoryWorkout>){
        val update = getCurrentViewStateOrNew()
        update.listHistoryWorkouts = history
        setViewState(update)
    }

    private fun setTotalHistory(total : Int){
        val update = getCurrentViewStateOrNew()
        update.totalHistoryWorkout = total
        setViewState(update)
    }

    *//********************************************************************
    ListHistory Page Management
     *********************************************************************//*

    private fun resetPage(){
        val update = getCurrentViewStateOrNew()
        update.page = 1
        setViewState(update)
    }

    private fun incrementPageNumber(){
        val update = getCurrentViewStateOrNew()
        val page = update.copy().page ?: 1
        update.page = page.plus(1)
        setViewState(update)
    }

    fun nextPageHistory(){
        if(!isQueryExhausted()){
            incrementPageNumber()
            setStateEvent(HistoryStateEvent.GetHistoryWorkoutsEvent())
        }
    }

    fun isQueryExhausted(): Boolean{
        return getCurrentViewStateOrNew().isQueryExhausted?: true
    }

    fun setQueryExhausted(isExhausted : Boolean){
        val update = getCurrentViewStateOrNew()
        update.isQueryExhausted = isExhausted
        setViewState(update)
    }
    fun refreshSearchQuery(){
        setQueryExhausted(false)
        loadHistory()
    }

    fun loadTotalHistory(){
        setStateEvent(HistoryStateEvent.GetTotalHistoryWorkoutsEvent())
    }

    fun loadHistory(){
        setStateEvent(HistoryStateEvent.GetHistoryWorkoutsEvent())
    }
*/
}