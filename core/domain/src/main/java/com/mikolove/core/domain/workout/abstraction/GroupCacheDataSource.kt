package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group

interface GroupCacheDataSource {

    suspend fun getGroups(idUser : String) : List<Group>

    suspend fun getGroup(groupId : String) : Group

    suspend fun upsertGroup(group : Group, idUser: String) : Long

    suspend fun deleteGroups(groups : List<Group>,idUser : String) : Int
}