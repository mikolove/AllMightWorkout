package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadingInteractors
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel
@Inject
constructor(
    private val loadingInteractors: LoadingInteractors,
    private val networkSyncManager: NetworkSyncManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    val state : MutableLiveData<LoadingState> = MutableLiveData(LoadingState())

    init {
    }

    fun onTriggerEvent(event : LoadingEvents){
        when(event){

            is LoadingEvents.GetAccountPreferences->{
                getAccountPreferences()
            }
            is LoadingEvents.CheckLastSessionStatus->{
                checkLastSessionStatus()
            }

            is LoadingEvents.UpdateSplashScreen->{
                //updateSplashScreen()
            }

            is LoadingEvents.OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
            LoadingEvents.Login -> {}
            is LoadingEvents.RegisterUser -> {

            }
        }
    }

    fun hasSyncBeenExecuted() = networkSyncManager.hasSyncBeenExecuted

    private fun syncCacheWithNetwork() {
        networkSyncManager.executeDataSync(viewModelScope)
    }

    /*
    Fun
     */
/*    private fun updateSplashScreen(){
        printLogD("LoadingViewModel","Update splashScreen")
        state.value?.let { state ->
            this.state.value = state.copy(splashScreenDone = true)
        }
    }*/

    private fun getAccountPreferences(){
        state.value?.let { state ->
            loadingInteractors.getAccountPreferences.execute()
                .onEach { dataState ->

                    dataState.data?.let { accountPreference ->
                        this.state.value = state.copy(accountPreference = accountPreference)
                        onTriggerEvent(LoadingEvents.CheckLastSessionStatus)
                    }

                    dataState.message?.let {  message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
        }
    }


    private fun checkLastSessionStatus(){
        state.value?.let { state ->
            sessionManager.state.value?.logged?.let { sessionStatus ->
                this.state.value = state.copy(lastSessionStatus = sessionStatus)
            }
        }
    }


/*state.accountPreference?.let{ accountPreference ->

    when(accountPreference.accountType){

        AccountType.BASIC ->{

        }
        AccountType.GOOGLE->{

        }
        else -> { }
    }*/

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
                printLogD("LoadingViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        state.value?.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    printLogD("LoadingViewModel","Added to queue message")
                    queue.add(messageBuild)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }


}