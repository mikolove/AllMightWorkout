package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.business.domain.state.DataState

sealed class LoadingEvents {

    data class SignInResult(val dataState : DataState<User>) : LoadingEvents()

    object GetAccountPreferences : LoadingEvents()

    data class LoadStep(val loadingStep: LoadingStep) : LoadingEvents()

    data class LoadUser(val idUser : String, val emailUser : String?, val name : String?) : LoadingEvents()

    data class SyncEverything(val user : User) : LoadingEvents()

    object UpdateSplashScreen : LoadingEvents()

    object Login : LoadingEvents()

    object OnRemoveHeadFromQueue: LoadingEvents()
}