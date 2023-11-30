package com.mikolove.allmightworkout.framework.presentation.session

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val serverClientId: String,
    private val oneTapClient: SignInClient,
    private val auth : FirebaseAuth,
    private val userFactory: UserFactory
) {

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): DataState<User> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            user?.let {
                DataState.data (
                    message = GenericMessageInfo.Builder()
                        .id("GoogleAuth.Success")
                        .title("GoogleAuth success")
                        .description("Google auth successfully retrieve user")
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = userFactory.createUser(
                            idUser = it.uid,
                            name = it.displayName,
                            email = it.email
                        )
                )
            } ?: DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GoogleAuth.NoUser")
                    .title("GoogleAuth no user")
                    .description("Google auth failed retrieving user")
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None)
            )

        } catch(e: Exception) {
            e.printStackTrace()
            //if(e is CancellationException) throw e
            DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GoogleAuth.Error")
                    .title("GoogleAuth error")
                    .description("Google auth an error occured retrieving user")
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None)
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        userFactory.createUser(
            idUser = uid,
            name = displayName,
            email = email
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(serverClientId)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}