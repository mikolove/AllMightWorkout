package com.mikolove.auth.data

import com.mikolove.auth.domain.AuthError
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
import com.mikolove.core.domain.auth.AuthManager


class AuthRepositoryImpl(
    private val auth : AuthManager,
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource : UserNetworkDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): EmptyResult<DataError> {

        val resultNetwork = auth.signInWithEmailAndPassword(email, password)

        if(resultNetwork !is Result.Success){
            sessionStorage.set(null)
            return Result.Error(AuthError.NO_FIREBASE_USER)
        }

        val userCreated = resultNetwork.data

        //Check if user exist in firestore DB
        var userInNetwork = safeApiCall {
            userNetworkDataSource.getUser(userCreated.idUser)
        }
        //if user does not exist in network db create it
        if (userInNetwork is Result.Success) {
            if (userInNetwork.data == null) {
                safeApiCall {
                    userNetworkDataSource.upsertUser(
                        User.create(
                            idUser = userCreated.idUser,
                            email = userCreated.email,
                            name = userCreated.name
                        )
                    )
                }
            }
        }

        //Check data in cache
        var userInCache = safeCacheCall {
            userCacheDataSource.getUser(userCreated.idUser)
        }

        //If user does not exist in cache create it
        if (userInCache is Result.Success) {
            if (userInCache.data === null) {
                safeCacheCall {
                    userCacheDataSource.upsertUser(
                        User.create(
                            idUser = userCreated.idUser,
                            email = userCreated.email,
                            name = userCreated.name
                        )
                    )
                }
            }
        }

        //Check if user exist in firestore DB
        userInNetwork = safeApiCall {
            userNetworkDataSource.getUser(userCreated.idUser)
        }

        userInCache = safeCacheCall {
            userCacheDataSource.getUser(userCreated.idUser)
        }

        if (userInNetwork is Result.Success && userInCache is Result.Success) {

            if (userInNetwork.data == null || userInCache.data == null) {
                sessionStorage.set(null)
                return Result.Error(AuthError.USER_NOT_EXIST)
            }

            //Save auth info
            sessionStorage.set(
                AuthInfo(userId = userCreated.idUser)
            )

            return Result.Success(Unit)

        } else {
            //Could not login
            sessionStorage.set(null)
            return Result.Error(AuthError.CHECKING_USER)
        }
    }

    override suspend fun signUp(email: String, password: String): EmptyResult<DataError> {

        val resultNetwork = auth.signInWithEmailAndPassword(email,password)

        if(resultNetwork !is Result.Success)
            return Result.Error(AuthError.NO_FIREBASE_USER)

        val userCreated = resultNetwork.data

        //Save user data
        val insertResult = safeCacheCall {
            userCacheDataSource.upsertUser(
                User.create(
                    idUser = userCreated.idUser,
                    email = userCreated.email,
                    name = userCreated.name
                )
            )
        }

        //Save user data in network => need to  add a sync feature if fail for any reason
        val saveNetwork = safeApiCall {
            userNetworkDataSource.upsertUser(
                User.create(
                    idUser = userCreated.idUser,
                    email = userCreated.email,
                    name = userCreated.name
                )
            )
        }

        if(insertResult is Result.Success && saveNetwork is Result.Success){
            return Result.Success(Unit)
        }else{
            return Result.Error(AuthError.SAVING_USER)
        }



    }

    override suspend fun signUpWithGoogle(
        email: String,
        password: String
    ): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }
}