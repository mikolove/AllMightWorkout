package com.mikolove.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.auth.domain.AuthRepository
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.auth.AuthInfo
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.abstraction.UserCacheDataSource
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.tasks.await

data class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val userCacheDataSource: UserCacheDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String): EmptyResult<DataError> {

        val resultNetwork = safeApiCall {
            auth.createUserWithEmailAndPassword(email, password).await()
        }

        if(resultNetwork !is Result.Success)
            return resultNetwork.asEmptyDataResult()

        val userCreated = resultNetwork.data.user

        if(userCreated != null){

            //Save auth info
            sessionStorage.set(
                AuthInfo(userId = userCreated.uid)
            )

            //Save user data
            val insertResult = safeCacheCall {
                userCacheDataSource.upsertUser(
                    User.create(
                        idUser = userCreated.uid,
                        email = userCreated.email,
                        name = userCreated.displayName
                    )
                )
            }

            if(insertResult is Result.Success){
                return Result.Success(Unit)
            }else{
                return insertResult.asEmptyDataResult()
            }

        }else{
            return Result.Error(DataError.Network.UNKNOWN)
        }

    }

    override suspend fun signUpWithGoogle(
        email: String,
        password: String
    ): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }
}