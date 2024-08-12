package com.mikolove.core.domain.workout

import com.mikolove.allmightworkout.business.domain.model.Group

interface GroupNetworkDataSource {

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun insertWorkoutGroup(group: Group)

    suspend fun updateWorkoutGroup(group: Group)

    suspend fun deleteWorkoutGroup(group : Group)

}