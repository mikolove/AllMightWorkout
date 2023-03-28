package com.mikolove.allmightworkout.business.data.network.abstraction

interface AuthNetworkDataSource {

    fun isUserAuthenticated() : Boolean

    suspend fun signIn()

    suspend fun signOut()

    fun getAuthState()
}