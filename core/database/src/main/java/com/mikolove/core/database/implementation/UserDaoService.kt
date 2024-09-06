package com.mikolove.core.database.implementation

import com.mikolove.core.database.database.UserDao
import com.mikolove.core.database.mappers.toUser
import com.mikolove.core.database.mappers.toUserCacheEntity
import com.mikolove.core.domain.user.User
import com.mikolove.core.domain.user.abstraction.UserCacheService

class UserDaoService(
    private val userDao : UserDao,
    ) : UserCacheService{

    override suspend fun upsertUser(user: User): Long {
        return userDao.upsertUser(user.toUserCacheEntity())
    }

    override suspend fun getUser(primaryKey: String): User? {
        return userDao.getUser(primaryKey)?.toUser()
    }
}