package com.mikolove.core.data.repositories.user

import com.mikolove.core.domain.user.abstraction.UserCacheDataSource
import com.mikolove.core.domain.user.abstraction.UserCacheService
import com.mikolove.core.domain.user.User

class UserCacheDataSourceImpl(
    private val userCacheService : UserCacheService
) : UserCacheDataSource {

    override suspend fun upsertUser(user: User): Long = userCacheService.upsertUser(user)

    override suspend fun getUser(primaryKey: String): User? = userCacheService.getUser(primaryKey)
}