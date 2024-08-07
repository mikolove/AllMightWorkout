package com.mikolove.allmightworkout.framework.presentation.session

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


class GoogleAuthUiClient(
    private val serverClientId: String,
    private val oneTapClient: SignInClient,
    private val auth : FirebaseAuth
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

    suspend fun signInWithIntent(intent: Intent): DataState<FirebaseUser> {
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
                    data = it
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
            .setAutoSelectEnabled(false)
            .build()
    }
}