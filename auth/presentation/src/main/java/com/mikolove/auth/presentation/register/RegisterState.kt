package com.mikolove.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState

data class RegisterState (
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false
)