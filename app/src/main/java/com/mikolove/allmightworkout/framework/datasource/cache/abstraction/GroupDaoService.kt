package com.mikolove.allmightworkout.framework.datasource.cache.abstraction

import com.mikolove.allmightworkout.business.domain.model.Group

interface GroupDaoService {

    suspend fun upsertWorkoutGroup(group : Group) : Long

    suspend fun getWorkoutGroups() : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun deleteWorkoutGroup(groups : Group) : Int
}