package com.mikolove.allmightworkout.business.interactors.main.auth

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class SignOut(
    private val firebaseAuth: FirebaseAuth
) {

    fun execute(): Flow<DataState<Boolean>> = flow {
        try {
            firebaseAuth.signOut().apply {
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
            emit(DataState.data(
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