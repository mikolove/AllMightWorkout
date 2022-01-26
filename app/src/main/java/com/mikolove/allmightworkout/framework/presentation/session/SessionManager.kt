package com.mikolove.allmightworkout.framework.presentation.session

import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.business.data.cache.abstraction.UserCacheDataSource
import com.mikolove.allmightworkout.business.data.datastore.AppDataStoreManager
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

class SessionManager
@Inject
constructor(
    private val sessionInteractors : SessionInteractors,
    private val userCacheDataSource: UserCacheDataSource,
    private val appDataStoreManager: AppDataStoreManager){

    private val sessionScope = CoroutineScope(Main)

    val state : MutableLiveData<SessionState> = MutableLiveData(SessionState())

    init {

    }

    fun onTriggerEvent(event : SessionEvents){
        when(event){

            is SessionEvents.Login->{
                login()
            }
            is SessionEvents.Logout->{

            }
            is SessionEvents.GetUser->{

            }
            is SessionEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
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