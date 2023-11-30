package com.mikolove.allmightworkout.business.interactors.sync

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
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.lang.Exception

/*
    Get last historyWorkout from cache ( XX last )
    Get Last historyWorkout from network ( XX last )
    for each not in network, search it in cache if they exist delete them from selected cache before.
    Then insert the cachedNote that were not deleted to the network

 */

class SyncHistory(
    private val historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
    private val historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource
) {

    suspend fun execute(
        idUser : String
    ) : DataState<SyncState> {

        //Get network log
        val networkResult = safeApiCall(IO){
            historyWorkoutNetworkDataSource.getLastHistoryWorkouts()
        }

        val response = object : ApiResponseHandler<List<HistoryWorkout>,List<HistoryWorkout>>(
            response = networkResult
        ){
            override suspend fun handleSuccess(resultObj: List<HistoryWorkout>): DataState<List<HistoryWorkout>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        if(response.message?.messageType == MessageType.Error){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncHistory.Error")
                    .title(SyncEverything.SYNC_GERROR_TITLE)
                    .description(SyncEverything.SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None),
                data = SyncState.FAILURE
            )

        }else{

            try{

                val cacheResult = safeCacheCall(IO){
                    historyWorkoutCacheDataSource.getHistoryWorkouts("","",1, idUser)
                }

                val cacheResponse = object : CacheResponseHandler<List<HistoryWorkout>, List<HistoryWorkout>>(
                    response = cacheResult,
                ){
                    override suspend fun handleSuccess(resultObj: List<HistoryWorkout>): DataState<List<HistoryWorkout>> {
                        return DataState.data(
                            message = null,
                            data = resultObj,
                        )
                    }
                }.getResult()

                val networkHistoryWorkouts = response.data ?: listOf()
                val cachedHistoryWorkouts = cacheResponse.data?.toMutableList() ?: mutableListOf()

                //If network log exist in cache just remove from arrayList
                //If not insert it
                //val job = launch {
                for(historyWorkout in networkHistoryWorkouts){
                    historyWorkoutCacheDataSource.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)?.let { cachedHistoryWorkout ->
                        cachedHistoryWorkouts.remove(cachedHistoryWorkout)
                    }?: insertHistoryWorkoutToCache(historyWorkout,idUser)
                }
                //}

                //Wait for it to be done
                //job.join()

                //Update network if needed with missing cache datas
                for(cachedHistoryWorkout in cachedHistoryWorkouts){
                    historyWorkoutNetworkDataSource.insertHistoryWorkout(cachedHistoryWorkout)
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncHistory.Success")
                        .title(SYNC_HIS_TITLE)
                        .description(SYNC_HIS_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (e: Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncHistory.Error")
                        .title(SYNC_HIS_ERROR_TITLE)
                        .description(SYNC_HIS_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.FAILURE
                )

            }
        }
    }


    private suspend fun insertHistoryWorkoutToCache(historyWorkout : HistoryWorkout,idUser : String){

        //Insert History Workout
        historyWorkoutCacheDataSource.insertHistoryWorkout(historyWorkout,idUser)

        //Insert History Exercises
        historyWorkout.historyExercises?.forEach { historyExercise ->

            historyExerciseCacheDataSource.insertHistoryExercise(historyExercise,historyWorkout.idHistoryWorkout)

            //Insert History Exercise Sets
            historyExercise.historySets.forEach{ historyExerciseSet ->
                historyExerciseSetCacheDataSource.insertHistoryExerciseSet(
                    historyExerciseSet,
                    historyExercise.idHistoryExercise)
            }

        }

    }
    companion object{
        val SYNC_HIS_TITLE = "Sync success"
        val SYNC_HIS_DESCRIPTION = "Successfully sync history"

        val SYNC_HIS_ERROR_TITLE = "Sync error"
        val SYNC_HIS_ERROR_DESCRIPTION = "Failed retrieving history. Check internet or try again later."

    }

}