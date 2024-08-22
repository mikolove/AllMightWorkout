package com.mikolove.core.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.core.network.abstraction.AuthFirestoreService

class AuthFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth
) : com.mikolove.core.network.abstraction.AuthFirestoreService {

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