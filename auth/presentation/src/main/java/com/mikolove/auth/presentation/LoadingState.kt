package com.mikolove.auth.presentation

import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class LoadingState(

    val isLoading :Boolean = false,

    val loadStatusText : String = "",

    val loadingStep : LoadingStep = LoadingStep.FIRST_LAUNCH,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)