package com.mikolove.allmightworkout.business.data.cache.implementation

import com.mikolove.allmightworkout.business.data.cache.abstraction.UserCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.UserDaoService

class UserCacheDataSourceImpl(
    private val userDaoService : UserDaoService
) : UserCacheDataSource{

    override suspend fun insertUser(user: User): Long = userDaoService.insertUser(user)

    override suspend fun updateName(name: String,primaryKey: String): Int = userDaoService.updateName(name,primaryKey)

    override suspend fun getUser(primaryKey: String): User? = userDaoService.getUser(primaryKey)
}