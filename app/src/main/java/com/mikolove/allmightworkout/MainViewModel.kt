package com.mikolove.allmightworkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.core.domain.auth.SessionManager
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.loading.LoadingRepository
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.workout.GroupRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionManager: SessionManager,
    private val sessionStorage: SessionStorage,
    private val loadingRepository: LoadingRepository,
) : ViewModel(){

    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

        //Initial check
        viewModelScope.launch {

            state = state.copy(
                isLoggedIn = sessionStorage.get() != null
            )

            state = state.copy(isCheckingAuth = false)

            //state = state.copy(isLoadingData = true)
            val workoutTypes  = async{
                loadingRepository.loadWorkoutTypes()
            }

            val groups = async{
                loadingRepository.loadGroups()
            }

            val resultWorkoutTypes = workoutTypes.await()
            //TODO : Check where to put this sync because u will not be registered first use but u will next time logged so ...
            val resultGroups = groups.await()

            if(resultWorkoutTypes is Result.Success){
                    state = state.copy(isLoadingData = false, isWorkoutTypesChecked = true)
            }else{
                    state = state.copy(isLoadingData = false, isWorkoutTypesChecked = false)
                }
        }

        sessionManager.isAuthenticated().onEach { result ->
            when(result){
                is Result.Error -> {
                }
                is Result.Success ->{
                    if(result.data.isEmpty()){
                        sessionStorage.set(null)
                        state = state.copy(isLoggedIn = false)
                        eventChannel.send(MainEvent.Logout)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout(){
        viewModelScope.launch {
            sessionManager.signOut()
                .collect{ result ->
                    when(result){
                        is Result.Error -> {}
                        is Result.Success -> {}
                    }
                }
        }
    }
}