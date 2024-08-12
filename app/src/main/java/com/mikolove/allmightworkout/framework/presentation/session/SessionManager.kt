package com.mikolove.allmightworkout.framework.presentation.session

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mikolove.core.domain.user.User
import com.mikolove.core.interactors.session.SessionInteractors
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything
import com.mikolove.allmightworkout.framework.workers.SyncEverythingWorker
import com.mikolove.allmightworkout.util.printLogD
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.state.doesMessageAlreadyExistInQueue
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
    private val sessionInteractors : com.mikolove.core.interactors.session.SessionInteractors,
){

    private val sessionScope = CoroutineScope(Main)

    private val _state = MutableStateFlow(SessionState())
    val state : StateFlow<SessionState> = _state

    init {
        onTriggerEvent(SessionEvents.GetAuthState)
    }

    fun onTriggerEvent(event : SessionEvents){

        when(event){

            is SessionEvents.UpdateFirstLaunch->{
                updateFirstLaunch()
            }
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
    fun getUser() : User? = _state.value.user
    fun getUserId() : String? = _state.value.user?.idUser

    private fun updateFirstLaunch(){
        _state.value.let { state ->
            this._state.value = state.copy(firstLaunch = false)
        }
    }

    private fun syncEverything(){
        _state.value.let { state ->
            state.user?.let { user ->

                val constraints  = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<SyncEverythingWorker>()
                    .setInputData(workDataOf("USER_ID" to user.idUser))
                    .setConstraints(constraints)
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

                    var userM : User? = null
                    dataState.data?.let { user ->
                        //CHANGE THIS SHIT JUST FOR TEST
                        userM = user.copy(createdAt = "", updatedAt = "")
                    }

                    this._state.value = state.copy(user = userM, firstLaunch = false)

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