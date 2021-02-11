package com.mikolove.allmightworkout.business.interactors.main.home

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
    private val bodyPartCacheDataSource: BodyPartCacheDataSource
) {

    fun getBodyParts(
        query : String,
        filterAndOrder : String,
        page : Int,
        stateEvent : StateEvent
    ) : Flow<DataState<HomeViewState>?> = flow{

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            bodyPartCacheDataSource.getBodyParts(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<HomeViewState, List<BodyPart>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<BodyPart>): DataState<HomeViewState>? {

                var message : String? = GET_BODYPARTS_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_BODYPARTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HomeViewState(listBodyParts = ArrayList(resultObj)),
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