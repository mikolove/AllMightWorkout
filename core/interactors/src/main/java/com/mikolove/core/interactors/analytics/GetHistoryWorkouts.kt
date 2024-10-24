package com.mikolove.core.interactors.analytics

import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource


class GetHistoryWorkouts(
    private val analyticsCacheDataSource: AnalyticsCacheDataSource
) {

   /* fun getHistoryWorkouts(
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
*/
    companion object{
        val GET_HISTORY_WORKOUTS_SUCCESS = "Successfully retrieved list of history workouts."
        val GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS = "There are no history workouts that match that query."
        val GET_HISTORY_WORKOUTS_FAILED = "Failed to retrieve the list of history workouts."
    }
}