package com.mikolove.core.network.abstraction

interface AuthFirestoreService {
    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}