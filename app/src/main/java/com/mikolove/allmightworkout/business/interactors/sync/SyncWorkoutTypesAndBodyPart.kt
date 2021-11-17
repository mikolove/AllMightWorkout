package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutTypeNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.BodyPart
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


  /*  suspend fun syncWorkoutTypesAndBodyPart(){
        val cachedWorkoutTypes = getCachedWorkoutTypes()

        syncNetworkWithCache(cachedWorkoutTypes)
    }

    suspend fun getCachedWorkoutTypes() : List<WorkoutType>{

        val cacheResult = safeCacheCall(IO){
            workoutTypeCacheDataSource.getWorkoutTypes("","",1)
        }

        val response = object : CacheResponseHandler<List<WorkoutType>, List<WorkoutType>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<List<WorkoutType>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    suspend fun syncNetworkWithCache(cachedWorkoutTypes : List<WorkoutType>)
            = withContext(IO){

        val networkResult = safeApiCall(IO){
            workoutTypeNetworkDataSource.getAllWorkoutTypes()
        }

        val response = object : ApiResponseHandler<List<WorkoutType>, List<WorkoutType>>(
            response = networkResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<List<WorkoutType>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()


        val networkWorkoutTypes = response?.data ?: ArrayList()
        val job = launch {
            for(networkWorkoutType in networkWorkoutTypes){

                when(val cachedWorkoutType = workoutTypeCacheDataSource.getWorkoutTypeById(networkWorkoutType.idWorkoutType)) {

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
        }
        //Not sure if usefull to wait for the end.
        //This could be a mistake to not wait and launching other sync job
        //job.join()
    }

    suspend fun checkIfWorkoutTypeCacheRequiresUpdate(cachedWorkoutType : WorkoutType, networkWorkoutType : WorkoutType){
       if( cachedWorkoutType != networkWorkoutType){
           safeCacheCall(IO){
                workoutTypeCacheDataSource.updateWorkoutType(
                    idWorkoutType = cachedWorkoutType.idWorkoutType,
                    name = networkWorkoutType.name)
            }
        }
    }

    suspend fun checkIfBodyPartCacheRequiresUpdate(cachedBodyPart : BodyPart, networkBodyPart : BodyPart){
        if(cachedBodyPart != networkBodyPart){
            safeCacheCall(IO){
                bodyPartCacheDataSource.updateBodyPart(
                    idBodyPart = cachedBodyPart.idBodyPart,
                    name = networkBodyPart.name
                )
            }
        }
    }*/
}