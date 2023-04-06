package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.UserNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.UserFirestoreService

class UserNetworkDataSourceImpl(
    private val userFireStoreService : UserFirestoreService
) : UserNetworkDataSource{

    override suspend fun insertUser(user: User) = userFireStoreService.insertUser(user)

    override suspend fun updateName(name: String, primaryKey: String) = userFireStoreService.updateName(name,primaryKey)

    override suspend fun getUser(primaryKey: String): User? = userFireStoreService.getUser(primaryKey)

    override suspend fun getUserWithWorkouts(primaryKey: String): User? = userFireStoreService.getUserWithWorkouts(primaryKey)
}