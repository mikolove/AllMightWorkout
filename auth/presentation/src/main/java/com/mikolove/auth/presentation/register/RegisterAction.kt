package com.mikolove.auth.presentation.register

import com.mikolove.auth.domain.CredentialError
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult

sealed interface RegisterAction {

    data object OnSignInClick : RegisterAction

    data object OnSignUpClick : RegisterAction

    data class OnSaveCredentialClick(val result: EmptyResult<DataError>) : RegisterAction

    data object OnSignUpWithGoogleClick : RegisterAction

    data object OnTogglePasswordVisibilityClick : RegisterAction
}