package com.mikolove.core.data.repositories.user

import com.mikolove.core.domain.user.abstraction.AuthNetworkDataSource
import com.mikolove.core.domain.user.abstraction.AuthNetworkService

class AuthNetworkDataSourceImpl
constructor(
    private val authNetworkService : AuthNetworkService
) : AuthNetworkDataSource {

    override fun isUserAuthenticated() : Boolean = authNetworkService.isUserAuthenticated()

    override suspend fun signIn() = authNetworkService.signIn()

    override suspend fun signOut() = authNetworkService.signOut()

    override fun getAuthState() = authNetworkService.getAuthState()

}