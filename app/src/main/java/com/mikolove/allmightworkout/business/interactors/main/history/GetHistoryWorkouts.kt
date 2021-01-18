package com.mikolove.allmightworkout.business.interactors.main.history

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHistoryWorkouts(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
) {

    fun getHistoryWorkouts(
        query : String,
        filterAndOrder : String,
        page : Int,
        stateEvent : StateEvent
    ) : Flow<DataState<HistoryViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            historyWorkoutCacheDataSource.getHistoryWorkouts(
                query=query,
                filterAndOrder = filterAndOrder,
                page = page )
        }

        val response = object : CacheResponseHandler<HistoryViewState,List<HistoryWorkout>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<HistoryWorkout>): DataState<HistoryViewState>? {

                var message : String? = GET_HISTORY_WORKOUTS_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HistoryViewState(listHistoryWorkouts = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )

            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_HISTORY_WORKOUTS_SUCCESS = "Successfully retrieved list of history workouts."
        val GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS = "There are no history workouts that match that query."
        val GET_HISTORY_WORKOUTS_FAILED = "Failed to retrieve the list of history workouts."
    }
}