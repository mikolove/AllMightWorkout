package com.mikolove.allmightworkout

import com.mikolove.core.presentation.ui.UiText

sealed interface MainEvent{

    data object Logout : MainEvent
    data class Error(val error: UiText) : MainEvent
}