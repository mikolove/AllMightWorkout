package com.mikolove.core.interactors.common

import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource


class GetBodyParts(
     val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

   /* inline fun <reified ViewState> getBodyParts(
        query : String,
        filterAndOrder : String,
        page : Int,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow{

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            bodyPartCacheDataSource.getBodyParts(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<ViewState, List<BodyPart>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<BodyPart>): DataState<ViewState>? {

                var message : String? = GET_BODYPARTS_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_BODYPARTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                val viewState = when(ViewState::class){
                    WorkoutViewState::class -> WorkoutViewState(listBodyParts = ArrayList(resultObj))
                    ExerciseViewState::class -> ExerciseViewState(listBodyParts = ArrayList(resultObj))
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = viewState as ViewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }*/

    companion object{
        val GET_BODYPARTS_SUCCESS = "Successfully retrieved list of bodyparts."
        val GET_BODYPARTS_NO_MATCHING_RESULTS = "There are no bodyparts that match that query."
        val GET_BODYPARTS_FAILED = "Failed to retrieve the list of bodyparts."
    }

}