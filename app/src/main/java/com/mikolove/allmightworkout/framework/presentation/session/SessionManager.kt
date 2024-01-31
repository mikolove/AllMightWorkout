package com.mikolove.allmightworkout.framework.presentation.session

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything
import com.mikolove.allmightworkout.framework.workers.SyncEverythingWorker
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
    private val workManager: WorkManager,
    private val sessionInteractors : SessionInteractors,
){

    private val sessionScope = CoroutineScope(Main)

    private val _state = MutableStateFlow(SessionState())
    val state : StateFlow<SessionState> = _state

    init {
        onTriggerEvent(SessionEvents.GetAuthState)
    }

    fun onTriggerEvent(event : SessionEvents){

        when(event){

            is SessionEvents.GetAuthState->{
                getAuthState()
            }
            is SessionEvents.SyncEverything->{
                syncEverything()
            }
            is SessionEvents.SyncResult->{
                syncResult(event.workInfo)
            }
            is SessionEvents.Signout->{
                signOut(event.googleAuthUiClient)
            }
            is SessionEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    /*
        Fun
     */

    fun isAuth() : Boolean = _state.value.user != null
    fun getUser() : User? = _state.value.user
    fun getUserId() : String? = _state.value.user?.idUser

    private fun syncEverything(){
        _state.value.let { state ->
            state.user?.let { user ->
                val workRequest = OneTimeWorkRequestBuilder<SyncEverythingWorker>()
                    .setInputData(workDataOf("USER_ID" to user.idUser))
                    .build()
                workManager.enqueue(workRequest)
                this._state.value = state.copy(syncUUID = workRequest.id)
            }
        }
    }

    private fun syncResult(workInfo: WorkInfo){
        _state.value.let {state ->
            when(workInfo.state ){
                WorkInfo.State.SUCCEEDED ->{
                    appendToMessageQueue(
                        GenericMessageInfo.Builder()
                            .id("SyncEverything.Success")
                            .title(SyncEverything.SYNC_EV_SUCCESS_TITLE)
                            .description(SyncEverything.SYNC_EV_SUCCESS_DESCRIPTION)
                            .messageType(MessageType.Success)
                            .uiComponentType(UIComponentType.Toast)
                    )
                    this._state.value = state.copy(syncUUID = null)
                }

                WorkInfo.State.FAILED ->{
                    appendToMessageQueue(
                        GenericMessageInfo.Builder()
                            .id("SyncEverything.Error")
                            .title(SyncEverything.SYNC_GERROR_TITLE)
                            .description(SyncEverything.SYNC_GERROR_DESCRIPTION)
                            .messageType(MessageType.Error)
                            .uiComponentType(UIComponentType.Toast)
                    )
                    this._state.value = state.copy(syncUUID = null)
                }
                else ->{}
            }
        }
    }

    private fun getAuthState(){
        _state.value.let { state ->
            sessionInteractors.getAuthState
                .execute()
                .onEach{ dataState ->

                    this._state.value = dataState.data?.let { user ->
                        //CHANGE THIS SHIT JUST FOR TEST
                        val userM = user.copy(createdAt = "", updatedAt = "")
                        state.copy(user = userM, firstLaunch = false)
                    } ?:   state.copy(firstLaunch = false)

                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }
                .launchIn(sessionScope)
        }
    }

    private fun signOut(googleAuthUiClient : GoogleAuthUiClient){
        _state.value.let { state ->
            sessionInteractors.signOut
                .execute(googleAuthUiClient)
                .onEach { dataState ->
                    dataState.data?.let {result ->
                        if (result) {
                            this._state.value = state.copy(user = null)
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
        _state.value.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this._state.value = state.copy(queue = queue)
            }catch (e: Exception){
                printLogD("SessionManager","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        _state.value.let { state ->
            val queue = state.queue
            val messageBuild = message.build()

            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){

                if(messageBuild.uiComponentType !is UIComponentType.None){

                    printLogD("SessionManager","Added to queue  : ${messageBuild.id}")

                    queue.add(messageBuild)
                    this._state.value = state.copy(queue = queue)
                }
            }
        }
    }

}