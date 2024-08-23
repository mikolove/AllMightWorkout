package com.mikolove.core.domain.user

interface UserNetworkDataSource {

    suspend fun insertUser(user : User)

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUser(primaryKey: String) : User?

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}