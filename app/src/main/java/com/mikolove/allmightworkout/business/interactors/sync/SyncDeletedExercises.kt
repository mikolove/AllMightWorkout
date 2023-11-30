package com.mikolove.allmightworkout.business.interactors.sync

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
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything.Companion.SYNC_GERROR_DESCRIPTION
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything.Companion.SYNC_GERROR_TITLE
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception

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

        if(response.message?.messageType == MessageType.Error){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncDeletedExercise.Error")
                    .title(SYNC_GERROR_TITLE)
                    .description(SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None),
                data = SyncState.FAILURE
            )

        }else{

            try{

                val deletedExercisesFromNetwork = response.data ?: listOf()

                //Delete them from cache
                safeCacheCall(IO){
                    exerciseCacheDataSource.removeExercises(deletedExercisesFromNetwork)
                }

                return  DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedExercise.Success")
                        .title(SYNC_DEX_TITLE)
                        .description(SyncDeletedExercises.SYNC_DEX_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS)

            }catch (e : Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedExercise.Error")
                        .title(SyncDeletedExercises.SYNC_DEX_ERROR_TITLE)
                        .description(SyncDeletedExercises.SYNC_DEX_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.FAILURE
                )

            }

        }

    }


    companion object{

        val SYNC_DEX_TITLE = "Sync success"
        val SYNC_DEX_DESCRIPTION = "Successfully sync deleted exercises"

        val SYNC_DEX_ERROR_TITLE = "Sync error"
        val SYNC_DEX_ERROR_DESCRIPTION = "Failed retrieving exercises. Check internet or try again later."

    }
}