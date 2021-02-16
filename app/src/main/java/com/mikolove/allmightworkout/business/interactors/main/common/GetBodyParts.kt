package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetBodyParts(
     val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    inline fun <reified ViewState> getBodyParts(
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
                    HomeViewState::class -> HomeViewState(listBodyParts = ArrayList(resultObj))
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
    }

    companion object{
        val GET_BODYPARTS_SUCCESS = "Successfully retrieved list of bodyparts."
        val GET_BODYPARTS_NO_MATCHING_RESULTS = "There are no bodyparts that match that query."
        val GET_BODYPARTS_FAILED = "Failed to retrieve the list of bodyparts."
    }

}