package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.AuthNetworkDataSource
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.AuthFirestoreService

class AuthNetworkDataSourceImpl
constructor(
    private val firebaseAuthService : AuthFirestoreService
) : AuthNetworkDataSource{

    override fun isUserAuthenticated() : Boolean = firebaseAuthService.isUserAuthenticated()

    override suspend fun signIn() = firebaseAuthService.signIn()

    override suspend fun signOut() = firebaseAuthService.signOut()

    override fun getAuthState() = firebaseAuthService.getAuthState()

}