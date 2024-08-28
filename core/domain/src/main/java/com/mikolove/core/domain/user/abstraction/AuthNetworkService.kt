package com.mikolove.core.domain.user.abstraction

interface AuthNetworkService {
    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}