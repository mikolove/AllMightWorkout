package com.mikolove.auth.presentation

import com.mikolove.auth.domain.CredentialError
import com.mikolove.core.presentation.ui.UiText

fun CredentialError.asUiText() : UiText {
    return when(this){
        CredentialError.CREATE_EXCEPTION -> UiText.StringResource(R.string.credential_create_exception)
        CredentialError.CREATE_CANCELATION -> UiText.StringResource(R.string.credential_create_cancelation)
        CredentialError.GET_CANCELATION -> UiText.StringResource(R.string.credential_get_cancelation)
        CredentialError.GET_EXCEPTION -> UiText.StringResource(R.string.credential_get_exception)
        CredentialError.NOT_EXIST -> UiText.StringResource(R.string.credential_not_exist)
    }
}