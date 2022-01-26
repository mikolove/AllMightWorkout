package com.mikolove.allmightworkout.business.data.network.abstraction

import com.mikolove.allmightworkout.business.domain.model.User

interface UserNetworkDataSource {

    suspend fun updateName(name : String,primaryKey : String)

    suspend fun getUserWithWorkouts(primaryKey: String) : User?
}