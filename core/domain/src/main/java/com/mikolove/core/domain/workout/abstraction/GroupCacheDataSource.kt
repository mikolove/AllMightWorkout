package com.mikolove.core.domain.workout.abstraction

import com.mikolove.core.domain.workout.Group
import kotlinx.coroutines.flow.Flow

interface GroupCacheDataSource {

    fun getGroups(idUser : String) : Flow<List<Group>>

    suspend fun getGroup(groupId : String) : Group

    suspend fun upsertGroup(group : Group, idUser: String) : Long

    suspend fun upsertGroups(groups : List<Group>, idUser: String) : List<Long>

    suspend fun deleteGroups(groupIds : List<String>,idUser : String) : Int

    suspend fun deleteGroup(groupId : String,idUser : String) : Int
}