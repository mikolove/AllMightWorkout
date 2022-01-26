package com.mikolove.allmightworkout.framework.presentation.session

sealed class SessionEvents {

    object Login : SessionEvents()

    object Logout : SessionEvents()

    data class GetUser(val idUser : String) : SessionEvents()

    object OnRemoveHeadFromQueue: SessionEvents()
}