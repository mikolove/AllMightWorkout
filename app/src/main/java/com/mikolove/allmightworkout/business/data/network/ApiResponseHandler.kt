package com.mikolove.allmightworkout.business.data.network

import com.mikolove.allmightworkout.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.mikolove.allmightworkout.business.data.network.NetworkErrors.NETWORK_ERROR
import com.mikolove.allmightworkout.business.domain.state.*

abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>
){

    suspend fun getResult(): DataState<ViewState> {

        return when(response){

            is ApiResult.GenericError -> {
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("ApiResponseHandler.GenericError")
                        .title("Api response error")
                        .description("Reason: ${response.errorMessage}")
                        .uiComponentType(UIComponentType.Dialog)
                        .messageType(MessageType.Error)
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("ApiResponseHandler.NetworkError")
                        .title("Api response error")
                        .description("Reason: Reason: ${NETWORK_ERROR}")
                        .uiComponentType(UIComponentType.Dialog)
                        .messageType(MessageType.Error)
                )
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("ApiResponseHandler.NetworkDataNull")
                            .title("Api data null")
                            .description("Reason: ${NETWORK_DATA_NULL}")
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

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

}