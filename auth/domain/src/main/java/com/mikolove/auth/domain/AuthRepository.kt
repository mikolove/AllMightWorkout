package com.mikolove.auth.domain

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult

interface AuthRepository{

    suspend fun signIn(email : String,password :String) : EmptyResult<DataError>

    suspend fun signUp(email : String,password :String) : EmptyResult<DataError>

    suspend fun signUpWithGoogle(email : String,password :String) : EmptyResult<DataError>

}