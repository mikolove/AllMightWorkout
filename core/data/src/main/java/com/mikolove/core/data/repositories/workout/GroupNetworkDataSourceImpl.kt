package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.abstraction.GroupNetworkDataSource
import com.mikolove.core.domain.workout.abstraction.GroupNetworkService

class GroupNetworkDataSourceImpl(
    private val groupNetworkService : GroupNetworkService
) : GroupNetworkDataSource {

    override suspend fun deleteGroup(idGroup: String) = groupNetworkService.deleteWorkoutGroup(idGroup)

    override suspend fun getGroups(): List<Group> = groupNetworkService.getWorkoutGroups()

    override suspend fun upsertGroup(group: Group) = groupNetworkService.upsertGroup(group)

}