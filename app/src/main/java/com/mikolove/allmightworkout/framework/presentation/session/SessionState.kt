package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class SessionState(

    val isLoading :Boolean = false,

    val checkAuth : Boolean = false,

    val idUser : String? = null,

    val logged : SessionLoggedType? = SessionLoggedType.DISCONNECTED,

    val connectivityStatus: SessionConnectivityStatus = SessionConnectivityStatus.UNAVAILABLE,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)