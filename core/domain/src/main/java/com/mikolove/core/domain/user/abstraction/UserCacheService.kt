package com.mikolove.core.domain.user.abstraction

import com.mikolove.core.domain.user.User

interface UserCacheService {

    suspend fun upsertUser(user : User) : Long
    
    suspend fun getUser(primaryKey : String) : User?
}