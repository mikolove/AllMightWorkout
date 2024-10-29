package com.mikolove.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.auth.domain.AuthRepository
import com.mikolove.auth.domain.UserDataValidator
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel(){

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        //Checking credentials
        viewModelScope.launch {
            snapshotFlow { state.email.text }
                .collectLatest { text ->
                    val isValidEmail = userDataValidator.isValidEmail(text.toString())
                    state = state.copy(
                        isEmailValid = isValidEmail,
                        canRegister = isValidEmail
                                && state.passwordValidationState.isValidPassword
                                && !state.isRegistering
                    )
                }
        }
        viewModelScope.launch {
            snapshotFlow { state.password.text }
                .collectLatest { text ->
                    val passwordValidationState = userDataValidator.validatePassword(text.toString())
                    state = state.copy(
                        passwordValidationState = passwordValidationState,
                        canRegister = passwordValidationState.isValidPassword
                                && state.isEmailValid
                                && !state.isRegistering
                    )
                }
        }
    }

    fun onAction(action: RegisterAction){
        when(action) {

            RegisterAction.OnSignInClick -> {}
            RegisterAction.OnSignUpClick -> { register()}
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            RegisterAction.OnSignUpWithGoogleClick -> {}
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true, canRegister = false)
            val result = authRepository
                .signUp(
                    email = state.email.text.toString().trim(),
                    password = state.password.text.toString().trim())

            state =state.copy(isRegistering = false, canRegister = true)
            when(result){
                is Result.Error -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }
}