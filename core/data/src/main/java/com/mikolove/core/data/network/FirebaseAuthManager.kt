package com.mikolove.core.data.network

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.core.domain.auth.AuthManager
import com.mikolove.core.domain.auth.NetworkUser
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthManager(
    private val auth : FirebaseAuth
) : AuthManager {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkUser, DataError> {

        try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            val firebaseUser = result.user!!
            return Result.Success(firebaseUser.toNetworkUser())

        }catch(e: Exception) {
            if (e is CancellationException) throw e
            val stackTrace = e.printStackTrace()
            Timber.d(stackTrace.javaClass.name + " ${e.message}")
            return Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<NetworkUser, DataError> {

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!
            return Result.Success(firebaseUser.toNetworkUser())

        }catch(e: Exception) {
            if (e is CancellationException) throw e
            val stackTrace = e.printStackTrace()
            Timber.d(stackTrace.javaClass.name + " ${e.message}")
            return Result.Error(DataError.Network.UNKNOWN)
        }

    }
}