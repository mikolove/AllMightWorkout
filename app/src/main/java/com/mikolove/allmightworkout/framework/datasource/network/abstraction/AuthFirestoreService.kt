package com.mikolove.allmightworkout.framework.datasource.network.abstraction

interface AuthFirestoreService {
    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}