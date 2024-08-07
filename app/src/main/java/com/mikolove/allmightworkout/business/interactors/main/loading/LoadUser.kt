package com.mikolove.allmightworkout.business.interactors.main.loading

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.UserCacheDataSource
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.UserNetworkDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*

    state already_exist
          new_account

    Receive a USERID corresponding to firebaseAuth.
    1) Check if userId exist data online.
        a) no,
    2) Check if userId exist in room cache
        a) yes, update name with online data
        b) no, create user with online data

 */
class LoadUser(
    private val userCacheDataSource: UserCacheDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userFactory: UserFactory,
    private val dateUtil: DateUtil
) {

    fun execute(
        idUser : String,
        email : String?,
        name : String?
    ) : Flow<DataState<User?>> = flow {

        emit(DataState.loading())

        val userRegisteredOnline = userExistOnline(idUser)
        val userRegisteredLocal  = userExistInCache(idUser)

        if(userRegisteredOnline.message?.messageType?.equals(MessageType.Error) == true){

            emit(
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("LoadUserGlobalError")
                        .title(LOAD_USER_GLOBLAL_ERROR_TITLE)
                        .description(LOAD_USER_GLOBLAL_ERROR_DESC)
                        .uiComponentType(UIComponentType.Dialog)
                        .messageType(MessageType.Error)))

        }else{

            val userOnline = userRegisteredOnline.data
            val userLocal = userRegisteredLocal.data

            when {
                //Already user in same device
                //Return newest user
                userOnline != null && userLocal != null -> {

                    var userLoaded : User = userLocal.copy()
                    val onlineUpdatedAt = dateUtil.convertStringDateToDate(userOnline.updatedAt)
                    val cacheUpdatedAt = dateUtil.convertStringDateToDate(userLocal.updatedAt)

                    //Online newest last
                    if(onlineUpdatedAt.after(cacheUpdatedAt)){
                        userLoaded = userOnline.copy()
                    }

                    //Send success
                    emit(
                        DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("CreateUser.Success")
                            .title(LOAD_USER_TITLE)
                            .description(LOAD_USER_SUCCESS_SYNC)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = userLoaded
                    ))

                }
                //User changing device
                userOnline != null && userLocal == null -> {

                    val userCreated = createUser(userOnline)
                    if(userCreated.message?.description != CREATE_SUCCESS){
                        emit(
                            DataState.error(
                            message = GenericMessageInfo.Builder()
                                .id("LoadUserSync.Error")
                                .title(LOAD_USER_TITLE)
                                .description(LOAD_USER_FAILED_SYNC)
                                .uiComponentType(UIComponentType.Toast)
                                .messageType(MessageType.Error),
                        ))
                    }else{
                        emit(
                            DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("LoadUserSync.Success")
                                .title(LOAD_USER_TITLE)
                                .description(LOAD_USER_SUCCESS_SYNC)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = userOnline
                        ))
                    }

                }
                //New user
                userOnline == null && userLocal == null -> {

                    val newUser = userFactory.createUser(idUser = idUser, email = email, name = name)
                    val isCreated = createUser(newUser)

                    if(isCreated.message?.description == CREATE_SUCCESS){

                        emit(
                            DataState.data(
                            message = GenericMessageInfo.Builder()
                                .id("LoadUserCreate.Success")
                                .title(LOAD_USER_TITLE)
                                .description(LOAD_USER_SUCCESS_CREATE)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = newUser
                        ))

                        updateNetwork(newUser)

                    }else{

                        emit(
                            DataState.error(
                            message = GenericMessageInfo.Builder()
                                .id("LoadUserCreate.Error")
                                .title(LOAD_USER_TITLE)
                                .description(LOAD_USER_FAILED_CREATE)
                                .uiComponentType(UIComponentType.Toast)
                                .messageType(MessageType.Error)
                        ))

                    }

                }
                /*If online does not exist and cache exist ( sync problem )
                userOnline == null && userLocal != null -> {

                }*/
            }

        }


    }

    private suspend  fun userExistInCache(idUser: String) : DataState<User?> {
        val userExist = safeCacheCall(Dispatchers.IO){
            userCacheDataSource.getUser(idUser)
        }

        val response = object : CacheResponseHandler<User?, User?>(
            response = userExist
        ){
            override suspend fun handleSuccess(resultObj: User?): DataState<User?> {

                return  DataState.data(
                    message = null,
                    data = resultObj)

            }
        }.getResult()

        return response
    }

    private suspend fun userExistOnline(idUser : String) : DataState<User?> {

        val userExistOnlineCall = safeApiCall(Dispatchers.IO){
            userNetworkDataSource.getUser(idUser)
        }

        val userExistOnlineResponse = object : ApiResponseHandler<User?, User?>(
            userExistOnlineCall
        ){
            override suspend fun handleSuccess(resultObj: User?): DataState<User?> {

                return  DataState.data(
                    message = null,
                    data = resultObj)

            }
        }.getResult()

        return userExistOnlineResponse

    }

    private suspend fun updateNetwork( newUser : User){
        safeApiCall(Dispatchers.IO){
            userNetworkDataSource.insertUser(newUser)
        }
    }

    private suspend fun createUser(newUser : User) : DataState<Long?> {
        val createUser = safeCacheCall(Dispatchers.IO){
            userCacheDataSource.insertUser(newUser)
        }

        val response = object : CacheResponseHandler<Long?, Long>(
            response = createUser
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<Long?> {
                return if(resultObj>0){
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("LoadUserCreateSuccess")
                            .title("")
                            .description(CREATE_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = resultObj)
                }else{
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("LoadUserCreateFAILED")
                            .title("")
                            .description(CREATE_FAILED)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Error))
                }
            }
        }.getResult()

        return response
    }

    companion object{

        val LOAD_USER_TITLE = "Load user"
        val LOAD_USER_SUCCESS = "User successfully loaded"
        val LOAD_USER_FAILED  = "Failed loading user."
        val LOAD_USER_SUCCESS_CREATE = "Successfully created user."
        val LOAD_USER_FAILED_CREATE  = "Failed creating new user."
        val LOAD_USER_SUCCESS_SYNC = "Successfully sync user."
        val LOAD_USER_FAILED_SYNC = "Error during sync user."

        val CREATE_SUCCESS = "Success"
        val CREATE_FAILED = "Failed"

        val LOAD_USER_GLOBLAL_ERROR_TITLE = "Error loading user"
        val LOAD_USER_GLOBLAL_ERROR_DESC = "An error occured during the initialization of the session. Please try again later"


    }
}