package com.mikolove.core.domain.user.abstraction

import com.mikolove.core.domain.user.User

interface UserNetworkDataSource {

    suspend fun insertUser(user : User)

    suspend fun updateName(name : String)

    suspend fun getUser(primaryKey: String) : User?
}