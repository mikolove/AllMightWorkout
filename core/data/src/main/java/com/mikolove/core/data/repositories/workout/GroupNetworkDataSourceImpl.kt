package com.mikolove.core.data.repositories.workout

import com.mikolove.core.domain.workout.abstraction.GroupNetworkDataSource
import com.mikolove.core.domain.workout.abstraction.GroupNetworkService
import com.mikolove.core.domain.workout.Group

class GroupNetworkDataSourceImpl(
    private val groupNetworkService : GroupNetworkService
) : GroupNetworkDataSource {
    override suspend fun getWorkoutGroups(): List<Group> = groupNetworkService.getWorkoutGroups()

    override suspend fun insertWorkoutGroup(group: Group) = groupNetworkService.insertWorkoutGroup(group)

    override suspend fun updateWorkoutGroup(group: Group) = groupNetworkService.updateWorkoutGroup(group)
    override suspend fun deleteWorkoutGroup(group: Group) = groupNetworkService.deleteWorkoutGroup(group)
}