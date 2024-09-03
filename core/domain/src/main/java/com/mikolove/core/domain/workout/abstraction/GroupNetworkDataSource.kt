package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupNetworkDataSource {

    suspend fun upsertGroup(group: Group)

    suspend fun deleteGroup(idGroup : String)

    suspend fun getGroups() : List<Group>
}