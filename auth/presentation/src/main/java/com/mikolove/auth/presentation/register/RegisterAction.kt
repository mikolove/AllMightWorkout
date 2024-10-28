package com.mikolove.auth.presentation.register

sealed interface RegisterAction {

    data object OnSignInClick : RegisterAction

    data object OnSignUpClick : RegisterAction

    data object OnTogglePasswordVisibilityClick : RegisterAction
}