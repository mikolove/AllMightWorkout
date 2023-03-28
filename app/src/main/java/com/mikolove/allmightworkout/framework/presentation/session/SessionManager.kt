package com.mikolove.allmightworkout.framework.presentation.session

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.framework.presentation.common.DataStoreKeys
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val sessionInteractors : SessionInteractors,
    private val firebaseAuth: FirebaseAuth,
    private val appDataStoreManager: AppDataStore){

    private val sessionScope = CoroutineScope(Main)

    val state : MutableLiveData<SessionState> = MutableLiveData(SessionState())


    init {
        onTriggerEvent(SessionEvents.MonitorConnectivity)
        onTriggerEvent(SessionEvents.GetAuthState)
        onTriggerEvent(SessionEvents.LoadSessionPreference)
    }

    fun onTriggerEvent(event : SessionEvents){
        when(event){

            is SessionEvents.MonitorConnectivity->{
                updateConnectivityStatus()
            }
            is SessionEvents.GetAuthState->{
                getAuthState()
            }
            is SessionEvents.Login->{
                login(event.idUser)
            }
            is SessionEvents.Signout->{
                signout()
            }
            is SessionEvents.CheckAuth->{
                checkAuth(event.idUser)
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

    fun checkAuth(idUser: String) : Boolean{
        val user = firebaseAuth.currentUser
        return user != null &&
                state.value?.idUser != null &&
                user.uid == idUser
        state.value?.logged == SessionLoggedType.CONNECTED
    }

    fun isNetworkAvailable() : Boolean = state.value?.connectivityStatus == SessionConnectivityStatus.AVAILABLE

    fun isAuth() : Boolean = firebaseAuth.currentUser != null

    fun getUserId() : String? = firebaseAuth.currentUser?.uid

    private fun login(idUser : String){
        state.value?.let { state ->
            saveSessionPreference(idUser,SessionLoggedType.CONNECTED)
            this.state.value = state.copy(logged = SessionLoggedType.CONNECTED,idUser = idUser)
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
                .launchIn(sessionScope)
        }
    }

    private fun getAuthState(){
        state.value?.let { state ->
            sessionInteractors.getAuthState
                .execute()
                .onEach{ dataState ->
                    dataState.data?.let {
                        this.state.value = state.copy(logged = dataState.data)
                    }

                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
                .launchIn(sessionScope)
        }
    }

    private fun signout(){
        //May be duplicated with GetAuthState Callback Flow
        state.value?.let { state ->
            sessionInteractors.signOut
                .execute()
                .onEach { dataState ->
                    dataState.data?.let {result ->
                        if (!result) {
                            this.state.value = state.copy(logged = SessionLoggedType.DISCONNECTED, idUser = null)
                            saveSessionPreference("",SessionLoggedType.DISCONNECTED)
                        }
                    }
                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
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