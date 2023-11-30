package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class SyncDeletedWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource
) {


    suspend fun execute() : DataState<SyncState> {

        //Get all deletedWorkouts from network
        val apiResult = safeApiCall(Dispatchers.IO){
            workoutNetworkDataSource.getDeletedWorkouts()
        }

        val response = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response = apiResult,
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        if(response.message?.messageType == MessageType.Error){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncDeletedWorkouts.Error")
                    .title(SyncEverything.SYNC_GERROR_TITLE)
                    .description(SyncEverything.SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None),
                data = SyncState.FAILURE
            )


        }else{

            try{

                val deletedWorkoutsFromNetwork = response.data ?: ArrayList()

                //Delete them from cache
                safeCacheCall(Dispatchers.IO){
                    workoutCacheDataSource.removeWorkouts(deletedWorkoutsFromNetwork)
                }

                return  DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedWorkouts.Success")
                        .title(SYNC_DW_TITLE)
                        .description(SYNC_DW_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS)

            }catch (e : Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedWorkouts.Error")
                        .title(SYNC_DW_ERROR_TITLE)
                        .description(SYNC_DW_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.FAILURE
                )
            }

        }
    }

    companion object{

        val SYNC_DW_TITLE = "Sync success"
        val SYNC_DW_DESCRIPTION = "Successfully sync deleted workouts"

        val SYNC_DW_ERROR_TITLE = "Sync error"
        val SYNC_DW_ERROR_DESCRIPTION = "Failed retrieving workouts. Check internet or try again later."

    }
}