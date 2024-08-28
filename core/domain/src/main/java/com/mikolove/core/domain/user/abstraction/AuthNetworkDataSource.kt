package com.mikolove.core.domain.user.abstraction

interface AuthNetworkDataSource {

    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}