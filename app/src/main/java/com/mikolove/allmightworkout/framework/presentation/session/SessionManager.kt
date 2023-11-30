package com.mikolove.allmightworkout.framework.presentation.session

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.R
import com.mikolove.allmightworkout.business.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    @ApplicationContext context : Context,
    private val sessionInteractors : SessionInteractors,
    private val firebaseAuth: FirebaseAuth,
    private val userFactory: UserFactory,){

    private val sessionScope = CoroutineScope(Main)

    val state : MutableState<SessionState> = mutableStateOf(SessionState())

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            serverClientId = context.getString(R.string.web_client_id),
            oneTapClient = Identity.getSignInClient(context),
            auth = firebaseAuth,
            userFactory =userFactory
        )
    }

    init {
        onTriggerEvent(SessionEvents.MonitorConnectivity)
        onTriggerEvent(SessionEvents.GetAuthState)
    }

    fun onTriggerEvent(event : SessionEvents){
        when(event){

            is SessionEvents.MonitorConnectivity->{
                updateConnectivityStatus()
            }
            is SessionEvents.GetAuthState->{
                getAuthState()
            }
            is SessionEvents.Signout->{
                signOut()
            }
            is SessionEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    /*
        Fun
     */

    fun isNetworkAvailable() : Boolean = state.value?.connectivityStatus == SessionConnectivityStatus.AVAILABLE

    fun isAuth() : Boolean = state.value?.user != null

    fun getUserId() : String? = state.value?.user?.idUser

    fun getUserEmail() : String? = state.value?.user?.email

    fun getFirstLaunch() : Boolean = state.value?.firstLaunch ?: true

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

                    dataState.data?.let {user ->
                        this.state.value = state.copy(user = user, firstLaunch = false)
                    }

                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
                .launchIn(sessionScope)
        }
    }

    private fun signOut(){
        state.value?.let { state ->
            sessionInteractors.signOut
                .execute(googleAuthUiClient)
                .onEach { dataState ->
                    dataState.data?.let {result ->
                        if (result) {
                            this.state.value = state.copy(user = null)
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