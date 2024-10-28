package com.mikolove.core.data.datasource.user

import com.mikolove.core.domain.user.abstraction.UserNetworkDataSource
import com.mikolove.core.domain.user.abstraction.UserNetworkService
import com.mikolove.core.domain.user.User

class UserNetworkDataSourceImpl(
    private val userNetworkService : UserNetworkService
) : UserNetworkDataSource {

    override suspend fun upsertUser(user: User) = userNetworkService.upsertUser(user)

    override suspend fun getUser(primaryKey: String): User? = userNetworkService.getUser(primaryKey)

}