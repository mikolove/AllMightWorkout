package com.mikolove.core.interactors.auth

/*import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.UserFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetAuthState(
    private val firebaseAuth: FirebaseAuth,
    private val userFactory: UserFactory
) {
    fun execute() : Flow<DataState<User>> = callbackFlow {

        val authStateListener = FirebaseAuth.AuthStateListener { auth ->

            val authUser = auth.currentUser
            
            if(authUser != null ){

                val user = userFactory.createUser(
                    idUser = authUser.uid,
                    email = authUser.email,
                    name = authUser.displayName,
                )

                trySend(
                    DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("GetAuthState.Connected")
                        .title(GETAUTHSTATE_TITLE)
                        .description(GETAUTHSTATE_CONNECTED)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = user
                ))

            } else{

                trySend(
                    DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("GetAuthState.Disconnected")
                        .title(GETAUTHSTATE_TITLE)
                        .description(GETAUTHSTATE_DISCONNECTED)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = null
                ))
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    companion object{
        val GETAUTHSTATE_TITLE = "Firebase Auth State"
        val GETAUTHSTATE_CONNECTED = "Connected"
        val GETAUTHSTATE_DISCONNECTED = "Disconnected"

    }
}*/