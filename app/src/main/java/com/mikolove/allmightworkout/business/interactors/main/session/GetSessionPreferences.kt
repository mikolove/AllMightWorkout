/*
package com.mikolove.allmightworkout.business.interactors.main.session

import com.mikolove.core.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.framework.presentation.session.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetSessionPreferences
constructor(private val appDataStoreManager: AppDataStore) {

    fun execute() : Flow<DataState<SessionPreference>> = flow{

        val logged = appDataStoreManager.readValue(DataStoreKeys.SESSION_LOGGED)?.let { loggedType ->
            getSessionLoggedType(loggedType)
        }?: getSessionLoggedType(SessionLoggedType.DISCONNECTED.value)

        val lastUserId = appDataStoreManager.readValue(DataStoreKeys.SESSION_LAST_USER_ID)


        val sessionPreference = SessionPreference(
            logged = logged,
            lastUserId = lastUserId)

        emit(
            DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("GetSessionPreferences.Success")
                    .title("")
                    .description(GET_SESSION_PREFERENCE_SUCCESS)
                    .messageType(MessageType.Success)
                    .uiComponentType(UIComponentType.None),
                data = sessionPreference
            )
        )
    }.catch { e ->
        emit(
            DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GetSessionPreferences.Error")
                    .title("")
                    .description(GET_SESSION_PREFERENCE_FAILED)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Toast)
            )
        )
    }

    companion object{
        val GET_SESSION_PREFERENCE_SUCCESS = "Session preference successfully retrieved from datastore."
        val GET_SESSION_PREFERENCE_FAILED = "Error loading session preference from datastore."
    }
}*/
