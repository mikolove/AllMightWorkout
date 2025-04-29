package com.mikolove.core.data.network

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.core.domain.auth.SessionManager
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

data class FirebaseSessionManager(
    private val firebaseAuth: FirebaseAuth,
)  : SessionManager {

    override  fun isAuthenticated(): Flow<Result<String, DataError>> = callbackFlow {

        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(
                Result.Success( auth.currentUser?.uid ?: "")
            )
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override fun signOut(): Flow<EmptyResult<DataError>> = flow {
        try {
            firebaseAuth.signOut()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(DataError.Network.UNKNOWN))
        }
    }

}