package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.mikolove.allmightworkout.framework.presentation.session.SessionLoggedType

sealed class LoadingEvents {

    object GetAccountPreferences : LoadingEvents()

    data class RegisterUser(val idUser : String) : LoadingEvents()

    object UpdateSplashScreen : LoadingEvents()

    object Login : LoadingEvents()

    object OnRemoveHeadFromQueue: LoadingEvents()
}