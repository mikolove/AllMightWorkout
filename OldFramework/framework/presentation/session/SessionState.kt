package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.Queue
import java.util.UUID

data class SessionState(

    val isLoading:Boolean = false,

    val firstLaunch: Boolean = true,

    val user: User? = null,

    val syncUUID: UUID? = null,

    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf())
)