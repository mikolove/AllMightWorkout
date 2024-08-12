package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.bodypart.BodyPartCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.core.domain.workouttype.WorkoutTypeNetworkDataSource
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.workouttype.WorkoutType
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything.Companion.SYNC_GERROR_DESCRIPTION
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything.Companion.SYNC_GERROR_TITLE
import kotlinx.coroutines.Dispatchers.IO

/*
    - We can get everything workoutypes and bodyPart are really small amount of data
    - Query all the workoutypes in the cache.
    - Query all the workoutypes in the network.
    - Insert it if not existing
    - Update it otherwise if needed
 */

class SyncWorkoutTypesAndBodyPart(
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
    private val workoutTypeNetworkDataSource: WorkoutTypeNetworkDataSource,
    private val bodyPartCacheDataSource: BodyPartCacheDataSource
) {


    suspend fun execute() : DataState<SyncState> {

        val networkWorkoutTypes = getNetworkWorkoutTypes().data ?: listOf()
        val cachedWorkoutTypes = getCachedWorkoutTypes().data ?: listOf()

        if(networkWorkoutTypes.isEmpty()){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncWorkoutTypesAndBodyPart.GlobalError")
                    .title(SYNC_GERROR_TITLE)
                    .description(SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Dialog),
                data = SyncState.FAILURE
            )

        }else{

            try{

                for(networkWorkoutType in networkWorkoutTypes){
                    //when(val cachedWorkoutType = workoutTypeCacheDataSource.getWorkoutTypeById(networkWorkoutType.idWorkoutType)) {
                    val cachedWorkoutType = cachedWorkoutTypes.find { it.idWorkoutType == networkWorkoutType.idWorkoutType }
                    when(cachedWorkoutType) {

                        //WorkoutType don't exist - Insert it and linked bodyPart
                        null -> {
                            workoutTypeCacheDataSource.insertWorkoutType(networkWorkoutType)
                            networkWorkoutType.bodyParts?.let { bodyParts ->
                                bodyParts.forEach { bodyPart ->
                                    bodyPartCacheDataSource.insertBodyPart(bodyPart,networkWorkoutType.idWorkoutType)
                                }
                            }
                        }

                        //WorkoutType exist - Check it and linked bodyPart
                        else -> {

                            checkIfWorkoutTypeCacheRequiresUpdate(cachedWorkoutType, networkWorkoutType)
                            networkWorkoutType.bodyParts?.let {  networkBodyParts ->
                                networkBodyParts.forEach { networkBodyPart ->
                                    val cachedBodyPart = bodyPartCacheDataSource.getBodyPartById(networkBodyPart.idBodyPart)
                                    cachedBodyPart?.let {
                                        checkIfBodyPartCacheRequiresUpdate(cachedBodyPart, networkBodyPart)
                                    }?:bodyPartCacheDataSource.insertBodyPart(networkBodyPart,networkWorkoutType.idWorkoutType)
                                }
                            }
                        }
                    }
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkoutTypesAndBodyPart.Success")
                        .title(SYNC_WKT_BDP_TITLE)
                        .description(SYNC_WKT_BDP_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (exception : Exception){
                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkoutTypesAndBodyPart.GlobalError")
                        .title(SYNC_WKT_BDP_ERROR_TITLE)
                        .description(SYNC_WKT_BDP_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.Dialog),
                    data = SyncState.FAILURE
                )
            }
        }


    }


    suspend fun getNetworkWorkoutTypes() : DataState<List<WorkoutType>> {
        val networkResult = safeApiCall(IO){
            workoutTypeNetworkDataSource.getAllWorkoutTypes()
        }

        val response = object : ApiResponseHandler<List<WorkoutType>, List<WorkoutType>>(
            response = networkResult
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<List<WorkoutType>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        return response
    }

    suspend fun getCachedWorkoutTypes() : DataState<List<WorkoutType>> {

        val cacheResult = safeCacheCall(IO){
            workoutTypeCacheDataSource.getWorkoutTypes("","",1)
        }

        val response = object : CacheResponseHandler<List<WorkoutType>, List<WorkoutType>>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<List<WorkoutType>> {
                return DataState.data(
                    message = null,
                    data = resultObj)
            }
        }.getResult()

        return response
    }

    private suspend fun checkIfWorkoutTypeCacheRequiresUpdate(cachedWorkoutType : WorkoutType, networkWorkoutType : WorkoutType){
        if( cachedWorkoutType != networkWorkoutType){
            safeCacheCall(IO){
                workoutTypeCacheDataSource.updateWorkoutType(
                    idWorkoutType = cachedWorkoutType.idWorkoutType,
                    name = networkWorkoutType.name)
            }
        }
    }

    private suspend fun checkIfBodyPartCacheRequiresUpdate(cachedBodyPart : BodyPart, networkBodyPart : BodyPart){
        if(cachedBodyPart != networkBodyPart){
            safeCacheCall(IO){
                bodyPartCacheDataSource.updateBodyPart(
                    idBodyPart = cachedBodyPart.idBodyPart,
                    name = networkBodyPart.name
                )
            }
        }
    }

    companion object{
        val SYNC_WKT_BDP_TITLE = "Sync success"
        val SYNC_WKT_BDP_DESCRIPTION = "Successfully sync workouts types and body parts"

        val SYNC_WKT_BDP_ERROR_TITLE = "Sync error"
        val SYNC_WKT_BDP_ERROR_DESCRIPTION = "Failed retrieving workouts types and body parts. Check internet or try again later."


    }
}