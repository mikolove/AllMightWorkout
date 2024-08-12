package com.mikolove.core.data.workout

import com.mikolove.core.domain.workout.GroupNetworkDataSource
import com.mikolove.core.domain.workout.Group
import com.mikolove.allmightworkout.framework.datasource.network.abstraction.GroupFirestoreService

class GroupNetworkDataSourceImpl(
    private val groupFirestoreService : GroupFirestoreService
) : GroupNetworkDataSource {
    override suspend fun getWorkoutGroups(): List<Group> = groupFirestoreService.getWorkoutGroups()

    override suspend fun insertWorkoutGroup(group: Group) = groupFirestoreService.insertWorkoutGroup(group)

    override suspend fun updateWorkoutGroup(group: Group) = groupFirestoreService.updateWorkoutGroup(group)
    override suspend fun deleteWorkoutGroup(group: Group) = groupFirestoreService.deleteWorkoutGroup(group)
}