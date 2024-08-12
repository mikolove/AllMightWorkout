package com.mikolove.core.interactors.loading/*
package com.mikolove.allmightworkout.business.interactors.main.loading

import com.mikolove.core.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.framework.presentation.main.loading.AccountPreference
import com.mikolove.allmightworkout.framework.presentation.main.loading.AccountType
import com.mikolove.allmightworkout.framework.presentation.main.loading.getAccountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetAccountPreferences
constructor(private val appDataStoreManager: AppDataStore) {

    fun execute() : Flow<DataState<AccountPreference>> = flow{

        */
/*val accountType = appDataStoreManager.readValue(DataStoreKeys.ACCOUNT_TYPE)?.let { accountType ->
            getAccountType(accountType)
        }?: AccountType.BASIC*//*


        val email = appDataStoreManager.readValue(DataStoreKeys.ACCOUNT_EMAIL)

        //val password = appDataStoreManager.readValue(DataStoreKeys.ACCOUNT_PASSWORD)

        val accountPreference = AccountPreference(
            //accountType = accountType,
            email = email,
        )

        emit(
            DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("GetAccountPreferences.Success")
                    .title("")
                    .description(GET_ACCOUNT_PREFERENCE_SUCCESS)
                    .messageType(MessageType.Success)
                    .uiComponentType(UIComponentType.None),
                data = accountPreference
            )
        )
    }.catch { e ->
        emit(
            DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GetAccountPreferences.Error")
                    .title("")
                    .description(GET_ACCOUNT_PREFERENCE_FAILED)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Toast)
            )
        )
    }

    companion object{
        val GET_ACCOUNT_PREFERENCE_SUCCESS = "Session preference successfully retrieved from datastore."
        val GET_ACCOUNT_PREFERENCE_FAILED = "Error loading session preference from datastore."
    }
}*/
