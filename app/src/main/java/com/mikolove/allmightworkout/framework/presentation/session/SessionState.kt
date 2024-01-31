package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue
import java.util.UUID

data class SessionState(

    val isLoading:Boolean = false,

    val firstLaunch: Boolean = true,

    val user: User? = null,

    val syncUUID: UUID? = null,

    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf())
)