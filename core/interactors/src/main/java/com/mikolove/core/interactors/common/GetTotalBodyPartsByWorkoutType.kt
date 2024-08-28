package com.mikolove.core.interactors.common

import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource

class GetTotalBodyPartsByWorkoutType(
    val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    /*inline fun <reified ViewState> getTotalBodyPartsByWorkoutType(
        idWorkoutType : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            bodyPartCacheDataSource.getTotalBodyPartsByWorkoutType(idWorkoutType)
        }

        val response = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {

                val viewState = when(ViewState::class){
                    WorkoutViewState::class -> WorkoutViewState(totalBodyPartsByWorkoutType = resultObj)
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS,
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
        val GET_TOTAL_BODYPART_BY_WORKOUTTYPE_SUCCESS = "Successfully retrieved the number of bodyparts by workout type from the cache."
        val GET_TOTAL_BODYPART_BY_WORKOUTTYPE_FAILED = "Failed to retrieved the number of bodyparts by workout type from the cache."
    }
}