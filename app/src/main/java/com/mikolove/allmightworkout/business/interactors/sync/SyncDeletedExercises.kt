package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers.IO

class SyncDeletedExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    suspend fun execute() : DataState<SyncState>{

        //Get all deletedExercises from network
        val apiResult = safeApiCall(IO){
            exerciseNetworkDataSource.getDeletedExercises()
        }

        val response = object : ApiResponseHandler<List<Exercise>,List<Exercise>>(
            response = apiResult,
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<List<Exercise>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        val deletedExercisesFromNetwork = response.data ?: listOf()

        //Delete them from cache
        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.removeExercises(deletedExercisesFromNetwork)
        }

        val cacheResponse = object :CacheResponseHandler<Int,Int>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {

                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        return  DataState.data(
            message = GenericMessageInfo.Builder()
                .id("SyncDeletedExercise.Success")
                .title(SyncDeletedExerciseSets.SYNC_DES_TITLE)
                .description(SyncDeletedExerciseSets.SYNC_DES_DESCRIPTION)
                .messageType(MessageType.Success)
                .uiComponentType(UIComponentType.None),
            da


    }

}