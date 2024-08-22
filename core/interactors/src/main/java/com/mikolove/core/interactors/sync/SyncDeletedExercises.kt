package com.mikolove.core.interactors.sync

import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.core.domain.exercise.ExerciseNetworkDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.interactors.sync.SyncEverything.Companion.SYNC_GERROR_DESCRIPTION
import com.mikolove.core.interactors.sync.SyncEverything.Companion.SYNC_GERROR_TITLE
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception

class SyncDeletedExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    suspend fun execute() : DataState<SyncState> {

        //Get all deletedExercises from network
        val apiResult = safeApiCall(IO){
            exerciseNetworkDataSource.getDeletedExercises()
        }

        val response = object : ApiResponseHandler<List<Exercise>, List<Exercise>>(
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
                        .description(SYNC_DEX_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (e : Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedExercise.Error")
                        .title(SYNC_DEX_ERROR_TITLE)
                        .description(SYNC_DEX_ERROR_DESCRIPTION)
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