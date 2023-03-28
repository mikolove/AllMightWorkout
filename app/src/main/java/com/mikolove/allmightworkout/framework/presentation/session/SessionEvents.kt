package com.mikolove.allmightworkout.framework.presentation.session

sealed class SessionEvents {

    object GetAuthState : SessionEvents()

    object MonitorConnectivity : SessionEvents()

    data class Login(val idUser : String) : SessionEvents()

    object Signout : SessionEvents()

    object LoadSessionPreference : SessionEvents()

    data class CheckAuth(val idUser: String) : SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}