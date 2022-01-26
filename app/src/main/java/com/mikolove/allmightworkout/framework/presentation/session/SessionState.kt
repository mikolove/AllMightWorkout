package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.Queue

data class SessionState(

    val isLoading :Boolean = false,

    val isAuth : Boolean = false,

    val lastUserId : String? = "",

    val user : User? = null,

    val queue : Queue<GenericMessageInfo> = Queue(mutableListOf())
)