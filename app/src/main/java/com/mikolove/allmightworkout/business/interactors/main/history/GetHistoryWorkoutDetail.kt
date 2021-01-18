package com.mikolove.allmightworkout.business.interactors.main.history

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHistoryWorkoutDetail(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
) {

    fun getHistoryWorkoutDetail(
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
    }

    companion object{
        val GET_HISTORY_WORKOUT_DETAIL_SUCCESS = "Successfully retrieved history workout with detail."

    }
}