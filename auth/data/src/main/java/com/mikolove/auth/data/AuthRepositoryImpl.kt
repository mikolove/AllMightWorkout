package com.mikolove.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.auth.domain.AuthRepository
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.auth.AuthInfo
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.abstraction.UserCacheDataSource
import com.mikolove.core.domain.user.abstraction.UserNetworkDataSource
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.tasks.await

data class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource : UserNetworkDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): EmptyResult<DataError> {

        val resultNetwork = safeApiCall {
            auth.signInWithEmailAndPassword(email, password).await()
        }

        if(resultNetwork !is Result.Success){
            sessionStorage.set(null)
            return resultNetwork.asEmptyDataResult()
        }

        val userCreated = resultNetwork.data.user

        if(userCreated != null){

            //Check data in cache
            val userInCache = safeCacheCall {
                userCacheDataSource.getUser(userCreated.uid)
            }

            //Check data in network
            val userInNetwork = safeApiCall {
                userNetworkDataSource.getUser(userCreated.uid)
            }

            if(userInCache is Result.Success && userInNetwork is Result.Success){

                if(userInCache.data == null || userInNetwork.data == null){
                    sessionStorage.set(null)
                    return Result.Error(DataError.Network.UNKNOWN)
                }

                //Save auth info
                sessionStorage.set(
                    AuthInfo(userId = userCreated.uid)
                )

                return Result.Success(Unit)

            }else{
                //Could not login
                sessionStorage.set(null)
                return Result.Error(DataError.Network.UNKNOWN)
            }
        }else{
            //Need custom auth error
            sessionStorage.set(null)
            return Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun signUp(email: String, password: String): EmptyResult<DataError> {

        val resultNetwork = safeApiCall {
            auth.createUserWithEmailAndPassword(email, password).await()
        }

        if(resultNetwork !is Result.Success)
            return resultNetwork.asEmptyDataResult()

        val userCreated = resultNetwork.data.user

        if(userCreated != null){

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

            //Save user data in network => need to  add a sync feature if fail for any reason
            val saveNetwork = safeApiCall {
                userNetworkDataSource.upsertUser(
                    User.create(
                        idUser = userCreated.uid,
                        email = userCreated.email,
                        name = userCreated.displayName
                    )
                )
            }

            if(insertResult is Result.Success && saveNetwork is Result.Success){
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