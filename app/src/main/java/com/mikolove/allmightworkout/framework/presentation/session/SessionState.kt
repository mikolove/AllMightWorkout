package com.mikolove.allmightworkout.framework.presentation.session

import android.text.BoringLayout
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class SessionState(

    val isLoading :Boolean = false,

    val firstLaunch : Boolean = true,

    val user : User? = null,

    val connectivityStatus: SessionConnectivityStatus = SessionConnectivityStatus.UNAVAILABLE,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)