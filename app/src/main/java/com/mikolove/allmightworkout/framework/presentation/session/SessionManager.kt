package com.mikolove.allmightworkout.framework.presentation.session

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.business.data.cache.abstraction.UserCacheDataSource
import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.data.datastore.AppDataStoreManager
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    @ApplicationContext context : Context,
    private val sessionInteractors : SessionInteractors,
    private val firebaseAuth: FirebaseAuth,
    private val appDataStoreManager: AppDataStore){

    private val sessionScope = CoroutineScope(Main)

    val state : MutableLiveData<SessionState> = MutableLiveData(SessionState())

    init {
        onTriggerEvent(SessionEvents.LoadSessionPreference)
        updateConnectivityStatus()
    }

    fun onTriggerEvent(event : SessionEvents){
        when(event){

            is SessionEvents.Login->{
                login(event.idUser)
            }
            is SessionEvents.Logout->{
                logout()
            }
            is SessionEvents.CheckAuth->{
                //checkAuth(event.idUser)
            }
            is SessionEvents.LoadSessionPreference->{
                loadSessionPreference()
            }
            is SessionEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    /*
        Fun
     */

    fun checkAuth() : Boolean{
        return firebaseAuth.currentUser != null && state.value?.idUser != null && state.value?.logged == SessionLoggedType.CONNECTED
    }

    fun isNetworkAvailable() : Boolean = state.value?.connectivityStatus == SessionConnectivityStatus.AVAILABLE

    private fun login(idUser : String){
        state.value?.let { state ->
            saveSessionPreference(idUser,SessionLoggedType.CONNECTED)
            this.state.value = state.copy(logged = SessionLoggedType.CONNECTED,idUser = idUser)
        }
    }

    private fun logout(){
        state.value?.let { state ->
            saveSessionPreference("",SessionLoggedType.DISCONNECTED)
            this.state.value = state.copy(logged = SessionLoggedType.DISCONNECTED,idUser = null)
        }
    }

    private fun saveSessionPreference(idUser: String, logged : SessionLoggedType)
    {
        sessionScope.launch {
            appDataStoreManager.setValue(DataStoreKeys.SESSION_LOGGED,logged.value)
            appDataStoreManager.setValue(DataStoreKeys.SESSION_LAST_USER_ID, idUser)
        }
    }

    private fun updateCheckAuth(){
        state.value?.let { state ->
            this.state.value = state.copy(checkAuth = true)
        }
    }

    private fun loadSessionPreference(){
        state.value?.let { state ->
            sessionInteractors.getSessionPreference
                .execute()
                .onEach { dataState ->
                    dataState.data?.let { data ->
                        this.state.value = state.copy(logged = data.logged, idUser = data.lastUserId)
                        data.lastUserId?.let {
                            onTriggerEvent(SessionEvents.CheckAuth(it))
                        }?: updateCheckAuth()

                    }
                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
        }
    }

    private fun updateConnectivityStatus(){
        printLogD("SessionManager","connectivity started")
        state.value?.let { state ->
            sessionInteractors.getSessionConnectivityStatus
                .execute()
                .onEach { dataState ->
                    dataState.data?.let { data ->
                        this.state.value = state.copy(connectivityStatus = data)
                    }
                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
                .onEach { printLogD("SessionManager","One call Or Not") }
                .launchIn(sessionScope)
        }
    }

    /*
        Queue managing
     */

    private fun removeHeadFromQueue(){
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            }catch (e: Exception){
                printLogD("SessionManager","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

}