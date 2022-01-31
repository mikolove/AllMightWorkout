package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class SessionState(

    val isLoading :Boolean = false,

    val logged : SessionLoggedType? = SessionLoggedType.DISCONNECTED,

    val checkAuth : Boolean = false,

    val idUser : String? = null,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)