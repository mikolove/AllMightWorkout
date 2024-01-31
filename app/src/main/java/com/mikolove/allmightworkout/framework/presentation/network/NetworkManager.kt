package com.mikolove.allmightworkout.framework.presentation.network

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.sync.SyncNetworkConnectivity
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager
@Inject
constructor(
    private val syncNetworkConnectivity: SyncNetworkConnectivity
){

    private val sessionScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(NetworkState())
    val state : StateFlow<NetworkState> = _state

    init {
        onTriggerEvent(NetworkEvents.GetNetworkStatus)
    }

    fun onTriggerEvent(event : NetworkEvents){

        when(event){

            is NetworkEvents.GetNetworkStatus->{
                getNetworkStatus()
            }

            is NetworkEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }


    private fun getNetworkStatus(){
        _state.value.let { state ->
            syncNetworkConnectivity
                .execute()
                .onEach {dataState ->
                    dataState.data?.let { networkStatus ->
                        this._state.value = state.copy(networkStatus = networkStatus)
                    }
                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(sessionScope)
        }
    }

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