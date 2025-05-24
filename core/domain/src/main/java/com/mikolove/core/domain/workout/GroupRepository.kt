package com.mikolove.core.domain.workout

import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface GroupRepository{
    fun getGroups() : Flow<List<Group>>

    suspend fun getGroup(groupId : String) : Result<Group, DataError>

    suspend fun upsertGroup(group : Group) : EmptyResult<DataError>

    suspend fun deleteGroup(groupId : String)
}