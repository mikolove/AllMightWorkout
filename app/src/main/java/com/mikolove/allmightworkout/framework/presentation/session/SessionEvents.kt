package com.mikolove.allmightworkout.framework.presentation.session

import com.mikolove.allmightworkout.business.domain.model.User

sealed class SessionEvents {

    object GetAuthState : SessionEvents()

    object MonitorConnectivity : SessionEvents()

    object Signout : SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}