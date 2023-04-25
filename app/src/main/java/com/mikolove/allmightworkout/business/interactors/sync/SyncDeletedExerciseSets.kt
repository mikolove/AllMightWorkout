package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO : don't call it FireStore has changed
class SyncDeletedExerciseSets(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {


    suspend fun execute() : DataState<SyncState> {

        //Get all deletedExercises from network
        val apiResult = safeApiCall(Dispatchers.IO){
            exerciseSetNetworkDataSource.getDeletedExerciseSets()
        }

        val response = object : ApiResponseHandler<List<ExerciseSet>, List<ExerciseSet>>(
            response = apiResult,
        ){
            override suspend fun handleSuccess(resultObj: List<ExerciseSet>): DataState<List<ExerciseSet>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        if(response.data?.isEmpty()){


        }else{


        }
        val deletedExercisesFromNetwork = response.data ?: listOf()

        if(!deletedExercisesFromNetwork.isEmpty()){
            //Delete them from cache
            val cacheResult = safeCacheCall(Dispatchers.IO){
                exerciseSetCacheDataSource.removeExerciseSets(deletedExercisesFromNetwork)
            }

            object : CacheResponseHandler<Int, Int>(
                response = cacheResult
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                    return DataState.data(
                        message = null,
                        data = resultObj
                    )
                }
            }.getResult()
        }

        return DataState.data(
            message = GenericMessageInfo.Builder()
                .id("SyncDeletedExerciseSets.Success")
                .title(SYNC_DES_TITLE)
                .description(SYNC_DES_DESCRIPTION)
                .messageType(MessageType.Success)
                .uiComponentType(UIComponentType.None),
            data = SyncState.SUCCESS
        )
    }


    companion object{

        val SYNC_DES_TITLE = "Sync success"
        val SYNC_DES_DESCRIPTION = "Successfully sync deleted exercises sets"

        val SYNC_DES_GERROR_TITLE = "Sync error"
        val SYNC_DES_GERROR_DESCRIPTION = "Failed retrieving exercises sets. Check internet or try again later."

    }
}