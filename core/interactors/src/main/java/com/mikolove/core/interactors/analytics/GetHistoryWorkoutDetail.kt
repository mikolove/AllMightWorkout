package com.mikolove.core.interactors.analytics

import com.mikolove.core.domain.analytics.abstraction.HistoryWorkoutCacheDataSource


class GetHistoryWorkoutDetail(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
) {

   /* fun getHistoryWorkoutDetail(
        idHistoryWorkout : String,
        stateEvent : StateEvent
    ) : Flow<DataState<HistoryViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            historyWorkoutCacheDataSource.getHistoryWorkoutById(idHistoryWorkout)
        }

        val response = object : CacheResponseHandler<HistoryViewState,HistoryWorkout>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: HistoryWorkout): DataState<HistoryViewState>? {

                return DataState.data(
                    response = Response(
                        message = GET_HISTORY_WORKOUT_DETAIL_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType =  MessageType.Success()
                    ),
                    data = HistoryViewState(historyWorkoutDetail = resultObj),
                    stateEvent = stateEvent
                )

            }
        }.getResult()

        emit(response)
    }*/

    companion object{
        val GET_HISTORY_WORKOUT_DETAIL_SUCCESS = "Successfully retrieved history workout with detail."

    }
}