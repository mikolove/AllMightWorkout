package com.mikolove.core.interactors.analytics

import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource

class GetTotalHistoryWorkouts( private val analyticsCacheDataSource: AnalyticsCacheDataSource
) {

   /* fun getTotalWorkouts(
        stateEvent: StateEvent
    ) : Flow<DataState<HistoryViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            historyWorkoutCacheDataSource.getTotalHistoryWorkout()
        }

        val response = object : CacheResponseHandler<HistoryViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<HistoryViewState>? {

                val viewState = HistoryViewState(
                    totalHistoryWorkout = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_HISTORY_WORKOUT_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }
*/
    companion object{
        val GET_TOTAL_HISTORY_WORKOUT_SUCCESS = "Successfully retrieved the number of history workouts from the cache."
        val GET_TOTAL_HISTORY_WORKOUT_FAILED = "Failed to retrieved the number of history workouts from the cache."
    }
}