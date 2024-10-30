package com.mikolove.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikolove.auth.domain.AuthRepository
import com.mikolove.auth.domain.UserDataValidator
import com.mikolove.core.domain.util.Result
import com.mikolove.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel(){

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine( snapshotFlow { state.email.text },  snapshotFlow { state.password.text }) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(
                    email = email.toString().trim()
                ) && password.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction){
        when(action){
            LoginAction.OnSignInClick -> { login() }
            LoginAction.OnTogglePasswordVisibility -> {
                Timber.d("VM VISIBLE")
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            is LoginAction.OnUseCredentialsClick ->{
                when(action.result){
                    is Result.Error -> {}
                    is Result.Success -> {
                        state = state.copy(
                            email = TextFieldState(initialText = action.result.data.username),
                            password = TextFieldState(initialText = action.result.data.password))
                    }
                }
            }
            else -> Unit

        }
    }

    private fun login(){
        Timber.d("LOGIN LAUNCH")

        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)

            val result = authRepository.signIn(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString().trim()
            )

            state = state.copy(isLoggingIn = false)

            when(result){
                is Result.Error -> {
                    eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}