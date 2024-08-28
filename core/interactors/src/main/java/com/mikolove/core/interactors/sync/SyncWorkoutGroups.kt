package com.mikolove.core.interactors.sync

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.core.data.workout.abstraction.GroupNetworkDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.workout.Group
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.util.DateUtil
import kotlinx.coroutines.Dispatchers.IO

class SyncWorkoutGroups(
    private val groupCacheDataSource: GroupCacheDataSource,
    private val groupNetworkDataSource: GroupNetworkDataSource,
    private val dateUtil: DateUtil
    ) {

    suspend fun execute() : DataState<SyncState> {

        val networkGroupsCall = safeApiCall(IO){
            groupNetworkDataSource.getWorkoutGroups()
        }

        val responseNetworkGroupsCall = object : ApiResponseHandler<List<Group>, List<Group>>(
            response = networkGroupsCall
        ){
            override suspend fun handleSuccess(resultObj: List<Group>): DataState<List<Group>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        val networkGroups = responseNetworkGroupsCall.data ?: listOf()

        val cacheGroupsCall = safeCacheCall(IO){
            groupCacheDataSource.getWorkoutGroups()
        }

        val responseCacheGroupsCall = object : CacheResponseHandler<List<Group>, List<Group>>(
            response = cacheGroupsCall
        ){
            override suspend fun handleSuccess(resultObj: List<Group>): DataState<List<Group>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        val cacheGroups = responseCacheGroupsCall.data ?: listOf()

        if(
            responseCacheGroupsCall.message?.messageType == MessageType.Error ||
            responseNetworkGroupsCall.message?.messageType == MessageType.Error ) {

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncWorkoutGroup.Failed")
                    .title(SYNC_WKG_ERROR_TITLE)
                    .description(SYNC_WKG_ERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None),
                data = SyncState.FAILURE
            )
            
        }

        //Sync networkgroups in cache
        for(networkGroup in networkGroups){

            //if(networkGroup in cacheGroups) {
            cacheGroups.find { group -> group.equals(networkGroup.idGroup) }?.let {cacheGroup ->
                //exist ?
                val cacheUpdatedAt = dateUtil.convertStringDateToDate(cacheGroup.updatedAt)
                val networkUpdatedAt = dateUtil.convertStringDateToDate(networkGroup.updatedAt)

                //update more recent
                if (cacheUpdatedAt.before(networkUpdatedAt)) {
                    //network group is more recent
                    groupCacheDataSource.upsertWorkoutGroup(networkGroup)
                } else {
                    //cache more recent
                    groupNetworkDataSource.updateWorkoutGroup(cacheGroup)
                }

            //}  else{
            } ?: run {
                //not exist ? insert network in cache
                groupCacheDataSource.upsertWorkoutGroup(networkGroup)
            }

        }

        //Upload cache in network
        for(cacheGroup in cacheGroups) {
            if( cacheGroup !in networkGroups){
                groupNetworkDataSource.insertWorkoutGroup(cacheGroup)
            }
        }

        return DataState.data(
            message = GenericMessageInfo.Builder()
                .id("SyncWorkoutGroup.Success")
                .title(SYNC_WKG_TITLE)
                .description(SYNC_WKG_DESCRIPTION)
                .messageType(MessageType.Success)
                .uiComponentType(UIComponentType.None),
            data = SyncState.SUCCESS
        )
    }

    companion object{
        val SYNC_WKG_TITLE = "Sync success"
        val SYNC_WKG_DESCRIPTION = "Successfully sync workouts groups"

        val SYNC_WKG_ERROR_TITLE = "Sync error"
        val SYNC_WKG_ERROR_DESCRIPTION = "Failed retrieving workouts groups. Check internet or try again later."


    }
}