package com.mikolove.allmightworkout.firebase.implementation

import com.google.firebase.auth.FirebaseAuth

class AuthFirestoreServiceImpl
constructor(
    private val firebaseAuth: FirebaseAuth
) : com.mikolove.allmightworkout.firebase.abstraction.AuthFirestoreService {

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