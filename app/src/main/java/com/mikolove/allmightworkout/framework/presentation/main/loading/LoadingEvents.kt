package com.mikolove.allmightworkout.framework.presentation.main.loading

sealed class LoadingEvents {

    object CheckLastSessionStatus : LoadingEvents()

    object GetAccountPreferences : LoadingEvents()

    data class RegisterUser(val idUser : String) : LoadingEvents()

    object UpdateSplashScreen : LoadingEvents()

    object Login : LoadingEvents()

    object OnRemoveHeadFromQueue: LoadingEvents()
}