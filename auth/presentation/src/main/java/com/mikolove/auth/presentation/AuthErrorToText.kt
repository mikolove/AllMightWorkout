package com.mikolove.auth.presentation

import com.mikolove.auth.domain.AuthError
import com.mikolove.core.presentation.ui.UiText

fun AuthError.asUiText() : UiText{
    return when(this){
        AuthError.SAVING_USER -> UiText.StringResource(R.string.auth_error_saving_user)
        AuthError.CHECKING_USER -> UiText.StringResource(R.string.auth_error_checking_user)
        AuthError.USER_NOT_EXIST -> UiText.StringResource(R.string.auth_error_user_not_exist)
        AuthError.NO_FIREBASE_USER -> UiText.StringResource(R.string.auth_error_no_firebase_user)
    }

}