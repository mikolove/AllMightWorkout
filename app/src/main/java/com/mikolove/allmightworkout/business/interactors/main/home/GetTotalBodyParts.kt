package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalBodyParts(
    private val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    fun getTotalBodyParts(
        stateEvent : StateEvent
    ): Flow<DataState<HomeViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            bodyPartCacheDataSource.getTotalBodyParts()
        }

        val response = object : CacheResponseHandler<HomeViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<HomeViewState>? {
                val viewState = HomeViewState(
                    numBodyParts = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_BODYPART_SUCCESS,
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
        val GET_TOTAL_BODYPART_SUCCESS = "Successfully retrieved the number of bodyparts from the cache."
        val GET_TOTAL_BODYPART_FAILED = "Failed to retrieved the number of bodyparts from the cache."
    }
}