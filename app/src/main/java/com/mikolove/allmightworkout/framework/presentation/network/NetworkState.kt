package com.mikolove.allmightworkout.framework.presentation.network

import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class NetworkState(

    val networkStatus: NetworkStatus = NetworkStatus.UNAVAILABLE,

    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf())
)
