package com.mikolove.core.interactors.auth

import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.allmightworkout.framework.presentation.session.GoogleAuthUiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignOut() {

    fun execute(
        googleAuthUiClient: GoogleAuthUiClient
    ): Flow<DataState<Boolean>> = flow {
        try {
            googleAuthUiClient.signOut().apply {
                emit(
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("SignOut.Success")
                            .title(SIGNOUT_TITLE)
                            .description(SIGNOUT_SUCCESS)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Success),
                        data = true
                    )
                )
            }
        } catch (e: Exception) {
            emit(
                DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SignOut.Failed")
                    .title(SIGNOUT_TITLE)
                    .description(SIGNOUT_FAILED)
                    .uiComponentType(UIComponentType.Toast)
                    .messageType(MessageType.Error),
                data = false
            ))
        }
    }

    companion object{
        val SIGNOUT_TITLE = "Sign out from firebase"
        val SIGNOUT_SUCCESS = "Successfully signout"
        val SIGNOUT_FAILED  = "Error during signout"

    }
}