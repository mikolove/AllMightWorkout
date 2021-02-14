package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalBodyPartsByWorkoutType(
    private val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    fun getTotalBodyPartsByWorkoutType(
        idWorkoutType : String,
        stateEvent : StateEvent
    ) : Flow<DataState<HomeViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            bodyPartCacheDataSource.getTotalBodyPartsByWorkoutType(idWorkoutType)
        }

        val response = object : CacheResponseHandler<HomeViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<HomeViewState>? {
                val viewState = HomeViewState(
                    totalBodyPartsByWorkoutType = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS,
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

    companion object{
        val GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS = "Successfully retrieved the number of bodyparts by workout type from the cache."
        val GET_TOTAL_BODYPART_BY_WORKOUTTYPE_FAILED = "Failed to retrieved the number of bodyparts by workout type from the cache."
    }
}