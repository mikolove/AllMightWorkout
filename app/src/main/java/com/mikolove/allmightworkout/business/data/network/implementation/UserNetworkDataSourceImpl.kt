package com.mikolove.allmightworkout.business.data.network.implementation

import com.mikolove.allmightworkout.business.data.network.abstraction.UserNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.User
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.UserFireStoreService

class UserNetworkDataSourceImpl(
    private val userFireStoreService : UserFireStoreService
) : UserNetworkDataSource{
    override suspend fun updateName(name: String, primaryKey: String) = userFireStoreService.updateName(name,primaryKey)

    override suspend fun getUserWithWorkouts(primaryKey: String): User? = userFireStoreService.getUserWithWorkouts(primaryKey)
}