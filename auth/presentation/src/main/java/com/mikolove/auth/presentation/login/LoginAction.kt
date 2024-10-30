package com.mikolove.auth.presentation.login

import com.mikolove.auth.domain.CredentialError
import com.mikolove.auth.domain.CredentialInfo
import com.mikolove.core.domain.util.Result

sealed interface LoginAction{
    data class OnUseCredentialsClick(val result : Result<CredentialInfo, CredentialError>) : LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnSignInClick : LoginAction
    data object OnSignUpClick :LoginAction
}