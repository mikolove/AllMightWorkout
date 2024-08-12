package com.mikolove.allmightworkout.framework.presentation.main.loading

import com.google.firebase.auth.FirebaseUser
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.state.DataState

sealed class LoadingEvents {

    data class SignInResult(val dataState : DataState<FirebaseUser>) : LoadingEvents()
    
    data class LoadStep(val loadingStep: LoadingStep) : LoadingEvents()

    data class LoadUser(val idUser : String, val emailUser : String?, val name : String?) : LoadingEvents()

    object SyncEverything : LoadingEvents()

    data class UpdateFirstLaunch(val firstLaunch : Boolean, val user: User?) : LoadingEvents()

    object OnRemoveHeadFromQueue: LoadingEvents()
}