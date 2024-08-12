package com.mikolove.core.domain.user

import com.mikolove.allmightworkout.business.domain.model.User

interface UserNetworkDataSource {

    suspend fun insertUser(user : User)

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUser(primaryKey: String) : User?

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}