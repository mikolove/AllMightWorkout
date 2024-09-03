package com.mikolove.core.domain.user.abstraction

import com.mikolove.core.domain.user.User

interface UserNetworkService {

    suspend fun upsertUser(user : User)

    suspend fun getUser(primaryKey: String) : User?

}