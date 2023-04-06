package com.mikolove.allmightworkout.framework.presentation.main.loading

sealed class LoadingEvents {


    object GetAccountPreferences : LoadingEvents()

    data class LoadUser(val idUser : String, val emailUser : String) : LoadingEvents()

    object UpdateSplashScreen : LoadingEvents()

    object Login : LoadingEvents()

    object OnRemoveHeadFromQueue: LoadingEvents()
}