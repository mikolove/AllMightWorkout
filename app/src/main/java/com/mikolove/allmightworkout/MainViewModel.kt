package com.mikolove.allmightworkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikolove.core.domain.auth.AuthInfo
import com.mikolove.core.domain.auth.SessionManager
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val sessionManager: SessionManager,
    private val sessionStorage: SessionStorage,
) : ViewModel(){

    var state by mutableStateOf(MainState())
        private set

    init {

        //Initial check auth
        viewModelScope.launch {

            state = state.copy(isCheckingAuth = true)
            state = state.copy(
                isLoggedIn = sessionStorage.get() != null
            )
            state = state.copy(isCheckingAuth = false)
        }

        //Firebase continuous check auth
        sessionManager.isAuthenticated().onEach { result ->
            Timber.d("FIREBASE STATUS CHANGED")
            when(result){
                is Result.Error -> {
                }
                is Result.Success ->{
                    if(result.data.isEmpty()){
                        sessionStorage.set(null)
                        state = state.copy(isLoggedIn = false)
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
                        is Result.Error -> {
                        }
                        is Result.Success -> {

                        }
                    }
                }
        }
    }

}