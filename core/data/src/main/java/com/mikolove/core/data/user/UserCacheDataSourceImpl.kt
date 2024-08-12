package com.mikolove.core.data.user

import com.mikolove.core.domain.user.UserCacheDataSource
import com.mikolove.core.domain.user.User
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.UserDaoService

class UserCacheDataSourceImpl(
    private val userDaoService : UserDaoService
) : UserCacheDataSource {

    override suspend fun insertUser(user: User): Long = userDaoService.insertUser(user)

    override suspend fun updateName(name: String,primaryKey: String): Int = userDaoService.updateName(name,primaryKey)

    override suspend fun getUser(primaryKey: String): User? = userDaoService.getUser(primaryKey)
}