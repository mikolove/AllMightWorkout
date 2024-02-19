package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.model.UserFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.domain.state.doesMessageAlreadyExistInQueue
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadingInteractors
import com.mikolove.allmightworkout.framework.presentation.main.loading.LoadingEvents.*
import com.mikolove.allmightworkout.framework.presentation.session.SessionEvents
import com.mikolove.allmightworkout.framework.presentation.session.SessionManager
import com.mikolove.allmightworkout.framework.presentation.session.SessionState
import com.mikolove.allmightworkout.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val loadingInteractors: LoadingInteractors,
    private val userFactory: UserFactory
) : ViewModel() {

    private val _state = MutableStateFlow(LoadingState())
    val state : StateFlow<LoadingState> = _state

    private val _sessionManagerState = sessionManager.state
    val sessionManagerState : StateFlow<SessionState> = _sessionManagerState

    fun onTriggerEvent(event : LoadingEvents){
        when(event){

            is LoadStep -> {
                updateLoadingStep(loadingStep = event.loadingStep)
            }
            is UpdateFirstLaunch->{
                updateFirstLaunch(event.firstLaunch,event.user)
            }
            is SignInResult -> {
                signInResult(dataState= event.dataState)
            }
            is LoadUser -> {
                loadUser(event.idUser,event.emailUser,event.name)
            }
            is SyncEverything ->{
                syncEverything()
            }
            is OnRemoveHeadFromQueue->{
                removeHeadFromQueue()
            }
        }
    }

    private fun updateLoadingStep(loadingStep: LoadingStep){
        _state.value.let { state ->
            this._state.value = state.copy(loadingStep = loadingStep)
        }
    }

    private fun signInResult(dataState : DataState<FirebaseUser>){
        _state.value.let { state ->

            this._state.value = state.copy(isLoading = dataState.isLoading)

            dataState.data?.let { firebaseUser ->
                val user = userFactory.createUser(
                    firebaseUser.uid,
                    firebaseUser.email,
                    firebaseUser.displayName
                )
                onTriggerEvent(LoadUser(user.idUser,user.email,user.name))
            }

            dataState.message?.let { message -> appendToMessageQueue(message) }
        }
    }

    private fun updateFirstLaunch(firstLaunch : Boolean, user : User?) {
        _state.value.let { state ->
            if(!firstLaunch){
                user?.let {
                    if(state.loadingStep == LoadingStep.FIRST_LAUNCH) {
                        onTriggerEvent(LoadStep(LoadingStep.CONNECTED))
                    }
                } ?:   onTriggerEvent(LoadStep(LoadingStep.NOT_CONNECTED))

            }else{

                if(state.loadingStep != LoadingStep.FIRST_LAUNCH)
                    onTriggerEvent(LoadStep(LoadingStep.FIRST_LAUNCH))
            }
        }
    }


    private fun loadUser(idUser : String, email : String?,name : String?){

        printLogD("LoadingViewModel","Start load user ")

        onTriggerEvent(LoadStep(LoadingStep.LOAD_USER))

        _state.value.let { state->

            loadingInteractors
                .loadUser
                .execute(idUser,email,name)
                .onEach { dataState ->

                    this._state.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { user ->
                        printLogD("LoadingViewModel","LoadUser Received Data Received ${user } / state ${state.loadingStep}")
                        onTriggerEvent(LoadStep(LoadingStep.LAUNCH_SYNC))
                    }

                    dataState.message?.let {message ->
                        appendToMessageQueue(message)
                    }
                }
                .launchIn(viewModelScope)
        }
    }


    private fun syncEverything(){
        sessionManager.onTriggerEvent(SessionEvents.SyncEverything)
        onTriggerEvent(LoadStep(LoadingStep.GO_TO_APP))
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
                printLogD("LoadingViewModel","Nothing to remove from queue")
            }
        }
    }

    private fun appendToMessageQueue(message: GenericMessageInfo.Builder){
        _state.value.let { state ->
            val queue = state.queue
            val messageBuild = message.build()
            if(!messageBuild.doesMessageAlreadyExistInQueue(queue = queue)){
                if(messageBuild.uiComponentType !is UIComponentType.None){
                    queue.add(messageBuild)
                    this._state.value = state.copy(queue = queue)
                }
            }
        }
    }


}