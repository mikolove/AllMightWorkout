package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers

//TODO : don't call it FireStore has changed
class SyncDeletedExerciseSets(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {


    suspend fun syncDeletedExerciseSets(){

        //Get all deletedExercises from network
        val apiResult = safeApiCall(Dispatchers.IO){
            exerciseSetNetworkDataSource.getDeletedExerciseSets()
        }

        val response = object : ApiResponseHandler<List<ExerciseSet>, List<ExerciseSet>>(
            response = apiResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<ExerciseSet>): DataState<List<ExerciseSet>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val deletedExercisesFromNetwork = response?.data ?: ArrayList()

        //Delete them from cache
        val cacheResult = safeCacheCall(Dispatchers.IO){
            exerciseSetCacheDataSource.removeExerciseSets(deletedExercisesFromNetwork)
        }

        object : CacheResponseHandler<Int, Int>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int>? {
                printLogD("SyncDeletedExerciseSets","exercise sets deleted : ${resultObj}")
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()
    }


}