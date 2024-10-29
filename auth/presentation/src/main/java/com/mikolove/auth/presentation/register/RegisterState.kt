package com.mikolove.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.mikolove.auth.domain.PasswordValidationState

data class RegisterState (
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val passwordValidationState : PasswordValidationState = PasswordValidationState(),
    val isPasswordVisible: Boolean = false,
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val isRegisteringGoogle: Boolean = false,
    val canRegisterGoogle: Boolean = true
)