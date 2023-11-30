package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue
import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType

data class LoadingState(

    val isLoading :Boolean = false,

    val loadStatusText : String = "",

    val loadingStep : LoadingStep = LoadingStep.INIT,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)