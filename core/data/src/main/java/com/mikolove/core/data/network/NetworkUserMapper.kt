package com.mikolove.core.data.network

import com.google.firebase.auth.FirebaseUser
import com.mikolove.core.domain.auth.NetworkUser

fun FirebaseUser.toNetworkUser(): NetworkUser {
    return NetworkUser(
        idUser = this.uid,
        email = this.email ?: "",
        name = this.displayName ?: ""
    )
}