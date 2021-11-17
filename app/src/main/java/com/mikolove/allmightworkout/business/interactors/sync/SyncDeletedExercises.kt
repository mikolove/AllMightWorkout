package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers.IO

class SyncDeletedExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

   /* suspend fun syncDeletedExercises(){

        //Get all deletedExercises from network
        val apiResult = safeApiCall(IO){
            exerciseNetworkDataSource.getDeletedExercises()
        }

        val response = object : ApiResponseHandler<List<Exercise>,List<Exercise>>(
            response = apiResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<List<Exercise>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val deletedExercisesFromNetwork = response?.data ?: ArrayList()

        //Delete them from cache
        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.removeExercises(deletedExercisesFromNetwork)
        }

        object :CacheResponseHandler<Int,Int>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int>? {
                printLogD("SyncDeletedExercises","exercises deleted : ${resultObj}")
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()
    }*/

}