package com.mikolove.core.interactors.sync

import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.interactors.sync.SyncEverything.Companion.SYNC_GERROR_DESCRIPTION
import com.mikolove.core.interactors.sync.SyncEverything.Companion.SYNC_GERROR_TITLE
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

//TODO : don't call it FireStore has changed
class SyncDeletedExerciseSets(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {


    suspend fun execute() : DataState<SyncState> {

        //Get all deletedExercises from network
        val apiResult = safeApiCall(Dispatchers.IO){
            exerciseSetNetworkDataSource.getDeletedExerciseSets()
        }

        val response = object : ApiResponseHandler<List<ExerciseSet>, List<ExerciseSet>>(
            response = apiResult,
        ){
            override suspend fun handleSuccess(resultObj: List<ExerciseSet>): DataState<List<ExerciseSet>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        if(response.message?.messageType == MessageType.Error){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncDeletedExerciseSets.Error")
                    .title(SYNC_GERROR_TITLE)
                    .description(SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.None),
                data = SyncState.FAILURE
            )

        }else{

            try {
                val deletedExercisesFromNetwork = response.data ?: listOf()

                if (deletedExercisesFromNetwork.isNotEmpty()) {
                    //Delete them from cache
                    safeCacheCall(Dispatchers.IO) {
                        exerciseSetCacheDataSource.removeExerciseSets(deletedExercisesFromNetwork)
                    }
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedExerciseSets.Success")
                        .title(SYNC_DES_TITLE)
                        .description(SYNC_DES_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (e : Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncDeletedExerciseSets.Error")
                        .title(SYNC_DES_ERROR_TITLE)
                        .description(SYNC_DES_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.FAILURE
                )

            }
        }
    }


    companion object{

        val SYNC_DES_TITLE = "Sync success"
        val SYNC_DES_DESCRIPTION = "Successfully sync deleted exercises sets"

        val SYNC_DES_ERROR_TITLE = "Sync error"
        val SYNC_DES_ERROR_DESCRIPTION = "Failed retrieving exercises sets. Check internet or try again later."

    }
}