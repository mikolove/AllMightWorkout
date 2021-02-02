package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.HistoryWorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
    Get last historyWorkout from cache ( XX last )
    Get Last historyWorkout from network ( XX last )
    for each not in network search it in cache if they exist delete them from selected cache before.
    Then insert the cachedNote that were not deleted to the network

 */

class SyncHistory(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
    private val historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource
) {



    suspend fun syncHistory() {

        val cachedHistoryWorkout = getLastCachedHistoryWorkouts()

        syncNetworkAndCache(cachedHistoryWorkouts = ArrayList(cachedHistoryWorkout))
    }

    private suspend fun syncNetworkAndCache(
        cachedHistoryWorkouts : ArrayList<HistoryWorkout>
    ) = withContext(IO){

        //Get network log
        val networkResult = safeApiCall(IO){
            historyWorkoutNetworkDataSource.getLastHistoryWorkouts()
        }

        val response = object : ApiResponseHandler<List<HistoryWorkout>,List<HistoryWorkout>>(
            response = networkResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<HistoryWorkout>): DataState<List<HistoryWorkout>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val networkHistoryWorkouts = response?.data ?: ArrayList()

        //If network log exist in cache just remove from arrayList
        //If not insert it
        val job = launch {
            for(historyWorkout in networkHistoryWorkouts){
                historyWorkoutCacheDataSource.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)?.let { cachedHistoryWorkout ->
                    cachedHistoryWorkouts.remove(cachedHistoryWorkout)
                }?: insertHistoryWorkoutToCache(historyWorkout)
            }
        }

        //Wait for it to be done
        job.join()

        //Update network if needed with missing cache datas
        for(cachedHistoryWorkout in cachedHistoryWorkouts){
            historyWorkoutNetworkDataSource.insertHistoryWorkout(cachedHistoryWorkout)
        }

    }

    private suspend fun insertHistoryWorkoutToCache(historyWorkout : HistoryWorkout){

        //Insert History Workout
        historyWorkoutCacheDataSource.insertHistoryWorkout(historyWorkout)

        //Insert History Exercises
        historyWorkout.historyExercises?.forEach { historyExercise ->

            historyExerciseCacheDataSource.insertHistoryExercise(historyExercise)

            //Insert History Exercise Sets
            historyExercise.historySets?.forEach{ historyExerciseSet ->
                historyExerciseSetCacheDataSource.insertHistoryExerciseSet(
                    historyExerciseSet,
                    historyExercise.idHistoryExercise)
            }

        }

    }

    private suspend fun getLastCachedHistoryWorkouts(): List<HistoryWorkout> {

        val cacheResult = safeCacheCall(IO){
            historyWorkoutCacheDataSource.getHistoryWorkouts("","",1)
        }

        val response = object : CacheResponseHandler<List<HistoryWorkout>, List<HistoryWorkout>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<HistoryWorkout>): DataState<List<HistoryWorkout>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }
}