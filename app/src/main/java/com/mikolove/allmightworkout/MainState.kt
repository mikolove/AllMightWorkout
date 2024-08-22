package com.mikolove.allmightworkout

import com.mikolove.allmightworkout.framework.presentation.network.NetworkStatus
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue

data class MainState(

    val isLoading : Boolean = false,

    val firstLaunch : Boolean = false,

    val isLoggedIn : Boolean = false,

    val networkStatus : NetworkStatus = NetworkStatus.UNAVAILABLE,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)