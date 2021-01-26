package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers

class SyncDeletedWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource
) {

    suspend fun syncDeletedWorkouts(){

        //Get all deletedWorkouts from network
        val apiResult = safeApiCall(Dispatchers.IO){
            workoutNetworkDataSource.getDeletedWorkouts()
        }

        val response = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response = apiResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val deletedWorkoutsFromNetwork = response?.data ?: ArrayList()

        //Delete them from cache
        val cacheResult = safeCacheCall(Dispatchers.IO){
            workoutCacheDataSource.removeWorkouts(deletedWorkoutsFromNetwork)
        }

        object : CacheResponseHandler<Int, Int>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int>? {
                printLogD("SyncDeletedWorkouts","workouts deleted : ${resultObj}")
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()
    }

}