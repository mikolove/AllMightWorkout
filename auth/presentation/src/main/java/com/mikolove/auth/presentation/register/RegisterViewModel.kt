package com.mikolove.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RegisterViewModel : ViewModel(){

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction){
        when(action) {

            RegisterAction.OnSignInClick -> {}
            RegisterAction.OnSignUpClick -> {}
            RegisterAction.OnTogglePasswordVisibilityClick -> {}
        }
    }
}