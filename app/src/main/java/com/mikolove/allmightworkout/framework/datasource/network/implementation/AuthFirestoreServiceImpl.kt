package com.mikolove.allmightworkout.framework.datasource.network.implementation

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.AuthFirestoreService

class AuthFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthFirestoreService{

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