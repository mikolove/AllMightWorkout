package com.mikolove.allmightworkout.firebase.abstraction

interface AuthFirestoreService {
    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}