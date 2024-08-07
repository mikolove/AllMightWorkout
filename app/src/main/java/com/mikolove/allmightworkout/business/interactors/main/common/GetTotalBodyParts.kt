package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource

class GetTotalBodyParts(
    val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    /*inline fun <reified ViewState> getTotalBodyParts(
        stateEvent : StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            bodyPartCacheDataSource.getTotalBodyParts()
        }

        val response = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {


                val viewState = when(ViewState::class) {
                    WorkoutViewState::class -> WorkoutViewState(
                        totalBodyParts = resultObj
                    )
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_BODYPART_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState as ViewState,
                    stateEvent = stateEvent
                )

            }
        }.getResult()

        emit(response)
    }*/

    companion object{
        val GET_TOTAL_BODYPART_SUCCESS = "Successfully retrieved the number of bodyparts from the cache."
        val GET_TOTAL_BODYPART_FAILED = "Failed to retrieved the number of bodyparts from the cache."
    }
}