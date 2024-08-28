package com.mikolove.core.data.repositories.user

import com.mikolove.core.domain.user.abstraction.UserNetworkDataSource
import com.mikolove.core.domain.user.abstraction.UserNetworkService
import com.mikolove.core.domain.user.User

class UserNetworkDataSourceImpl(
    private val userNetworkService : UserNetworkService
) : UserNetworkDataSource {

    override suspend fun insertUser(user: User) = userNetworkService.insertUser(user)

    override suspend fun updateName(name: String) = userNetworkService.updateName(name)

    override suspend fun getUser(primaryKey: String): User? = userNetworkService.getUser(primaryKey)

}