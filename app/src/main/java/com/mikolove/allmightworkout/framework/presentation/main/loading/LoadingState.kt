package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class LoadingState(

    val isLoading :Boolean = false,

    val loadStatusText : String = "",

    val loadingStep : LoadingStep = LoadingStep.FIRST_LAUNCH,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)