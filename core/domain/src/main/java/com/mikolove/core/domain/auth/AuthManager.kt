package com.mikolove.core.domain.auth

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.Result

interface AuthManager {

    suspend fun signInWithEmailAndPassword(email: String, password: String) : Result<NetworkUser,DataError>

    suspend fun createUserWithEmailAndPassword(email: String,password: String) : Result<NetworkUser,DataError>

}