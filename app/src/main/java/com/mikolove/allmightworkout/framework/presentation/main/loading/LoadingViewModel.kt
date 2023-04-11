package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadUser.Companion.LOAD_USER_SUCCESS_CREATE
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadUser.Companion.LOAD_USER_SUCCESS_SYNC
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadingInteractors
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingEvents.*
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
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val syncDeletedExercises: SyncDeletedExercises,
    private val syncDeletedWorkouts: SyncDeletedWorkouts,
    private val syncHistory: SyncHistory,
    private val syncExercises: SyncExercises,
    private val syncWorkouts: SyncWorkouts,
    private val syncWorkoutExercises: SyncWorkoutExercises
) : ViewModel() {

    val state : MutableLiveData<LoadingState> = MutableLiveData(LoadingState())

    init {
    }

    fun onTriggerEvent(event : LoadingEvents){
        when(event){

            is GetAccountPreferences->{
                getAccountPreferences()
            }
            is UpdateSplashScreen->{
                //updateSplashScreen()
            }
            is OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
            is Login -> {

            }
            is LoadStep -> {
                updateLoadingStep(loadingStep = event.loadingStep)
            }
            is LoadUser -> {
                loadUser(event.idUser,event.emailUser)
            }
            is SyncWorkoutTypesAndBodyParts->{
                syncWorkoutTypesAndBodyParts()
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

    private fun updateLoadingStep(loadingStep: LoadingStep){
        state.value?.let { state ->
            this.state.value = state.copy(loadingStep = loadingStep)
        }
    }

    private fun getAccountPreferences(){

        state.value?.let { state ->
            loadingInteractors.getAccountPreferences.execute()
                .onEach { dataState ->

                    dataState.data?.let { accountPreference ->
                        this.state.value = state.copy(accountPreference = accountPreference)
                    }

                    dataState.message?.let {  message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun loadUser(idUser : String, email : String){

        //updateLoadingStep(LoadingStep.LOAD_USER)

        state.value?.let { state->
            loadingInteractors
                .loadUser
                .execute(idUser,email)
                .onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let {user ->
                        this.state.value = state.copy(user = user)
                    }

                    dataState.message?.let {message ->

                        this.state.value = state.copy(loadStatusText = message.description ?: "")

                        when(message.description){

                            LOAD_USER_SUCCESS_CREATE -> {
                                updateLoadingStep(LoadingStep.LOADED_USER_CREATE)
                                onTriggerEvent(SyncWorkoutTypesAndBodyParts)
                            }

                            LOAD_USER_SUCCESS_SYNC -> {
                                updateLoadingStep(LoadingStep.LOADED_USER_SYNC)
                                onTriggerEvent(SyncWorkoutTypesAndBodyParts)
                            }
                            else ->{
                                updateLoadingStep(LoadingStep.INIT)
                            }
                        }

                        appendToMessageQueue(message)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun syncWorkoutTypesAndBodyParts(){

        state.value?.let { state ->
            syncWorkoutTypesAndBodyPart.execute()
                .onEach { dataState ->
                    this.state.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { SyncState ->

                        when(SyncState){

                            com.mikolove.allmightworkout.business.interactors.sync.SyncState.SUCCESS ->{
                                updateLoadingStep(LoadingStep.SYNC_DELETED_EXERCISESETS)
                            }

                            com.mikolove.allmightworkout.business.interactors.sync.SyncState.FAILURE ->{

                            }
                        }
                    }

                    dataState.message?.let { message ->
                        appendToMessageQueue(message)
                    }
                }.launchIn(viewModelScope)
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