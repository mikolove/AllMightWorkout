package com.mikolove.auth.presentation.register

import com.mikolove.core.presentation.ui.UiText

sealed interface RegisterEvent{
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}