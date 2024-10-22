package com.mikolove.core.network.firebase.implementation

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.core.domain.user.abstraction.AuthNetworkService

class AuthFirestoreService
constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthNetworkService {

    override fun isUserAuthenticated()  = firebaseAuth.currentUser != null

    override suspend fun signIn() {
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override fun getAuthState() {
        TODO("Not yet implemented")
    }
}