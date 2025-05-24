package com.mikolove.core.data.repositories

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.util.map
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.workout.GroupRepository
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.GroupNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GroupRepositoryImpl (
    private val groupCacheDataSource: GroupCacheDataSource,
    private val groupNetworkDataSource: GroupNetworkDataSource,
    private val sessionStorage : SessionStorage,
) : GroupRepository{

    override fun getGroups(): Flow<List<Group>> = flow {
        val userId = sessionStorage.get()?.userId ?: return@flow
        emitAll(groupCacheDataSource.getGroups(userId))
    }

    override suspend fun getGroup(groupId: String): Result<Group, DataError> {
        return safeCacheCall {
            groupCacheDataSource.getGroup(groupId)
        }.map { it }
    }

    override suspend fun upsertGroup(group: Group): EmptyResult<DataError> {
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        val groupCacheResult = safeCacheCall {
            groupCacheDataSource.upsertGroup(group,userId)
        }
        if(groupCacheResult !is Result.Success)
            return groupCacheResult.asEmptyDataResult()

        val networkResult = safeApiCall {
            groupNetworkDataSource.upsertGroup(group)
        }

        return when(networkResult){
            is Result.Error -> Result.Success(Unit)
            is Result.Success -> Result.Success(Unit)
        }
    }

    override suspend fun deleteGroup(groupId: String) {
        val userId = sessionStorage.get()?.userId ?: return

        safeCacheCall {
            groupCacheDataSource.deleteGroup(groupId,userId)
        }
    }
}