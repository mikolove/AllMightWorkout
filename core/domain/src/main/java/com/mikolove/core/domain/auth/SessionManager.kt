package com.mikolove.core.domain.auth

import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SessionManager {

     fun isAuthenticated() : Flow<Result<String, DataError>>

    fun signOut() : Flow<EmptyResult<DataError>>

}