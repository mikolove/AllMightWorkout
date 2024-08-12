package com.mikolove.core.domain.user

interface AuthNetworkDataSource {

    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}