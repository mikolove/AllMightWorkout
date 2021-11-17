package com.mikolove.allmightworkout.business.data.cache

import com.mikolove.allmightworkout.business.data.cache.CacheErrors.CACHE_DATA_NULL
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType


abstract class CacheResponseHandler <T, Data>(
    private val response: CacheResult<Data?>
){
    suspend fun getResult(): DataState<T>? {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("CacheResponseHandler.GenericError")
                        .title("Cache response error")
                        .description("Reason: ${response.errorMessage}")
                        .uiComponentType(UIComponentType.Dialog)
                        .messageType(MessageType.Error)
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("CacheResponseHandler.CacheDataNull")
                            .title("Cache data null")
                            .description("Reason: ${CACHE_DATA_NULL}.")
                            .uiComponentType(UIComponentType.Dialog)
                            .messageType(MessageType.Error)
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<T>?

}