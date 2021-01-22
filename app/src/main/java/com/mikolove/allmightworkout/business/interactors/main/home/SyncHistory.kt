package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryWorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/*
    Get Total history Workout on cache and network
    Get All historyWorkout on network based on last updated_at on cache.
    Get last history workout base on updated_at on cache.
    Query network for all network history workout superior to this history workout date.


 */

class SyncHistory(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
    private val historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource
) {



    suspend fun syncHistory() {}

    suspend fun syncNetworkAndCache(
        lastNetworkHistoryWorkout : HistoryWorkout?
    ) = withContext(IO){


    }
    suspend fun getLastNetworkHistoryWorkout() : HistoryWorkout?{

        val networkResult = safeApiCall(IO){
            historyWorkoutNetworkDataSource.getLastHistoryWorkout()
        }


        val response = object : ApiResponseHandler<HistoryWorkout,HistoryWorkout>(
            response = networkResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: HistoryWorkout): DataState<HistoryWorkout>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: null
    }

    suspend fun getLastCachedHistoryWorkout() : HistoryWorkout?{

    }
}