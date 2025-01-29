package com.mikolove.auth.data

import com.google.firebase.auth.FirebaseAuth
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
import com.mikolove.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource : UserNetworkDataSource,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): EmptyResult<DataError> {

        //Check if user exist in firebase User base
        val resultNetwork = safeApiCall {
            auth.signInWithEmailAndPassword(email, password).await()
        }

        if(resultNetwork !is Result.Success){
            sessionStorage.set(null)
            return resultNetwork.asEmptyDataResult()
        }

        val userCreated = resultNetwork.data.user

        //Yes continue
        if(userCreated != null){

            //Check if user exist in firestore DB
            var userInNetwork = safeApiCall {
                userNetworkDataSource.getUser(userCreated.uid)
            }
            //if user does not exist in network db create it
            if( userInNetwork is Result.Success){
                if(userInNetwork.data == null){
                    safeApiCall {
                        userNetworkDataSource.upsertUser(
                            User.create(
                                idUser = userCreated.uid,
                                email = userCreated.email,
                                name = userCreated.displayName
                            )
                        )
                    }
                }
            }

            //Check data in cache
            var userInCache = safeCacheCall {
                userCacheDataSource.getUser(userCreated.uid)
            }

            //If user does not exist in cache create it
            if(userInCache is Result.Success){
                if(userInCache.data === null){
                    safeCacheCall {
                        userCacheDataSource.upsertUser(
                            User.create(
                                idUser = userCreated.uid,
                                email = userCreated.email,
                                name = userCreated.displayName
                            )
                        )
                    }
                }
            }

            //Check if user exist in firestore DB
            userInNetwork = safeApiCall {
                userNetworkDataSource.getUser(userCreated.uid)
            }

            userInCache = safeCacheCall {
                userCacheDataSource.getUser(userCreated.uid)
            }

            if( userInNetwork is Result.Success && userInCache is Result.Success){

                if(userInNetwork.data == null || userInCache.data == null){
                    sessionStorage.set(null)
                    return Result.Error(AuthError.USER_NOT_EXIST)
                }

                //Save auth info
                sessionStorage.set(
                    AuthInfo(userId = userCreated.uid)
                )

                return Result.Success(Unit)

            }else{
                //Could not login
                sessionStorage.set(null)
                return Result.Error(AuthError.CHECKING_USER)
            }
        }else{
            //Need custom auth error
            sessionStorage.set(null)
            return Result.Error(AuthError.NO_FIREBASE_USER)
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
                return Result.Error(AuthError.SAVING_USER)
            }

        }else{
            return Result.Error(AuthError.NO_FIREBASE_USER)
        }

    }

    override suspend fun signUpWithGoogle(
        email: String,
        password: String
    ): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }
}