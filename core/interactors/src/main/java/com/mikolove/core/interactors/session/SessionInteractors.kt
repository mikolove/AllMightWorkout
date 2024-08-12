package com.mikolove.core.interactors.session

class SessionInteractors(
    val signOut: com.mikolove.core.interactors.auth.SignOut,
    val getAuthState: com.mikolove.core.interactors.auth.GetAuthState,
){
}