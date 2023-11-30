package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadUser.Companion.LOAD_USER_SUCCESS_CREATE
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadUser.Companion.LOAD_USER_SUCCESS_SYNC
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadingInteractors
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything
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
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val syncDeletedExercises: SyncDeletedExercises,
    private val syncDeletedWorkouts: SyncDeletedWorkouts,
    private val syncHistory: SyncHistory,
    private val syncExercises: SyncExercises,
    private val syncWorkouts : SyncWorkouts,
    private val syncWorkoutExercises: SyncWorkoutExercises
) : ViewModel() {

    val state : MutableState<LoadingState> = mutableStateOf(LoadingState())

    init {}

    fun onTriggerEvent(event : LoadingEvents){
        when(event){

            is Login ->{

            }
            is LoadingEvents.SyncEverything ->{
                syncEverything(event.user)
            }
            is GetAccountPreferences->{
                //getAccountPreferences()
            }
            is UpdateSplashScreen->{
                //updateSplashScreen()
            }
            is OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
            is SignInResult -> {
                signInResult(dataState= event.dataState)
            }
            is LoadStep -> {
                updateLoadingStep(loadingStep = event.loadingStep)
            }
            is LoadUser -> {
                loadUser(event.idUser,event.emailUser,event.name)
            }

        }
    }

    private fun updateLoadingStep(loadingStep: LoadingStep){
        state.value?.let { state ->
            this.state.value = state.copy(loadingStep = loadingStep)
        }
    }

    private fun signInResult(dataState : DataState<User>){
        state.value?.let { state ->

            this.state.value = state.copy(isLoading = dataState.isLoading)

            dataState.data?.let { user ->
                onTriggerEvent(LoadUser(user.idUser,user.email,user.name))
            }

            dataState.message?.let { message -> appendToMessageQueue(message) }
        }
    }

    private fun loadUser(idUser : String, email : String?,name : String?){

        //updateLoadingStep(LoadingStep.LOAD_USER)

        state.value?.let { state->
            loadingInteractors
                .loadUser
                .execute(idUser,email,name)
                .onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let {user ->
                        onTriggerEvent(LoadingEvents.SyncEverything(user))
                    }

                    dataState.message?.let {message ->

                        this.state.value = state.copy(loadStatusText = message.description ?: "")

                        when(message.description){

                            LOAD_USER_SUCCESS_CREATE -> {
                                onTriggerEvent(LoadStep(LoadingStep.LOADED_USER_CREATE))
                            }

                            LOAD_USER_SUCCESS_SYNC -> {
                                onTriggerEvent(LoadStep(LoadingStep.LOADED_USER_SYNC))
                            }
                            else ->{
                                onTriggerEvent(LoadStep(LoadingStep.INIT))
                            }
                        }

                        appendToMessageQueue(message)
                    }
                }
                .launchIn(viewModelScope)
        }
    }


    private fun syncEverything(user : User){

        state.value?.let { state ->

            printLogD("LoadingViewModel","testFlow ${user}")
            val userId = user.idUser

            printLogD("LoadingViewModel","User ID go SYNC ${userId}")

            val syncEverything = SyncEverything(
                syncWorkoutTypesAndBodyPart =  syncWorkoutTypesAndBodyPart,
                syncDeletedExerciseSets = syncDeletedExerciseSets,
                syncDeletedExercises= syncDeletedExercises,
                syncDeletedWorkouts= syncDeletedWorkouts,
                syncHistory= syncHistory,
                syncExercises= syncExercises,
                syncWorkouts = syncWorkouts,
                syncWorkoutExercises=syncWorkoutExercises)

            syncEverything(userId)
                .onEach { dataState ->

                    this.state.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { data ->
                        if(data == SyncState.SUCCESS){
                            onTriggerEvent(LoadStep(LoadingStep.GO_TO_APP))
                        }
                    }

                    dataState.message?.let {  message ->
                        appendToMessageQueue(message)
                    }
                }
                .launchIn(viewModelScope)
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