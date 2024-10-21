package com.mikolove.core.interactors.sync

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.network.ApiResponseHandler
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource
import kotlinx.coroutines.Dispatchers.IO
import java.text.SimpleDateFormat

class SyncWorkouts(
    private val dateFormat: SimpleDateFormat,
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
) {

    /*suspend fun execute(
        idUser: String
    ) : DataState<SyncState> {

        val cachedWorkouts = getCachedWorkouts(idUser).toMutableList()

        //Get all network workouts
        val apiResult = safeApiCall(IO){
            workoutNetworkDataSource.getWorkouts()
        }

        val apiResponse = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response =  apiResult,
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        if(apiResponse.message?.messageType == MessageType.Error){

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncExercises.GlobalError")
                    .title(SyncEverything.SYNC_GERROR_TITLE)
                    .description(SyncEverything.SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Dialog),
                data = SyncState.FAILURE
            )

        }else{
            try{

                val networkWorkouts = apiResponse.data ?: mutableListOf()

                //val job = launch {
                //Compare workout in network with cache and proceed update if needed
                for(networkWorkout in networkWorkouts){

                    workoutCacheDataSource.getWorkoutById(networkWorkout.idWorkout)?.also { cacheWorkout ->
                        //Update core workout
                        cachedWorkouts.remove(cacheWorkout)
                        updateWorkoutIfNeeded(networkWorkout,cacheWorkout)

                        *//*
                         TODO : Update link between exercises and workout need to figure how will it work with firebase and room relational model
                         cacheWorkout.exercises?.forEach{ cacheExercise ->
                         }
                        *//*

                    }?: insertWorkoutInCache(networkWorkout, idUser = idUser )
                }
                //}

                //job.join()

                //if workout from network not found in cache insert it in network
                for(notFoundWorkout in cachedWorkouts){
                    printLogD("SyncWorkouts","Insert into network")
                    workoutNetworkDataSource.insertWorkout(notFoundWorkout)
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkouts.Success")
                        .title(SYNC_W_TITLE)
                        .description(SYNC_W_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (e : Exception){

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkouts.Error")
                        .title(SYNC_W_ERROR_TITLE)
                        .description(SYNC_W_ERROR_DESCRIPTION)
                        .messageType(MessageType.Error)
                        .uiComponentType(UIComponentType.Dialog),
                    data = SyncState.FAILURE
                )
            }
        }

    }

    private suspend fun getCachedWorkouts(idUser : String) : List<Workout>{

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkouts("","",1,idUser)
        }

        val response = object : CacheResponseHandler<List<Workout>, List<Workout>>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        return response.data ?: ArrayList()
    }

    private suspend fun insertWorkoutInCache(networkWorkout : Workout, idUser : String) {
        printLogD("SyncWorkouts","Insert into cache")
        workoutCacheDataSource.insertWorkout(networkWorkout,idUser)
    }

    private suspend fun updateWorkoutIfNeeded(networkWorkout : Workout, cacheWorkout : Workout){

        val networkUpdatedAt = dateFormat.parse(networkWorkout.updatedAt)
        val cacheUpdatedAt = dateFormat.parse(cacheWorkout.updatedAt)

        //If network has newest data
        if(networkUpdatedAt != cacheUpdatedAt) {
            printLogD("SyncWorkouts","Try to update workout")

            //If network has newest data
            if (networkUpdatedAt != null) {
                if(networkUpdatedAt.after(cacheUpdatedAt)){
                    printLogD("SyncWorkouts","Workout in cache updated ${cacheWorkout.idWorkout}")
                    workoutCacheDataSource.updateWorkout(
                        networkWorkout.idWorkout,
                        networkWorkout.name,
                        networkWorkout.isActive,
                        networkWorkout.updatedAt
                    )

                }
            }
            //If cache has newest data
            if (networkUpdatedAt != null) {
                if(networkUpdatedAt.before(cacheUpdatedAt)) {
                    printLogD("SyncWorkouts","Workout in network updated ${cacheWorkout.idWorkout}")
                    workoutNetworkDataSource.updateWorkout(
                        cacheWorkout
                    )
                }
            }
        }
    }

    companion object{
        val SYNC_W_TITLE = "Sync success"
        val SYNC_W_DESCRIPTION = "Successfully sync workouts"

        val SYNC_W_ERROR_TITLE = "Sync error"
        val SYNC_W_ERROR_DESCRIPTION = "Failed retrieving workouts. Check internet or try again later."

    }*/
}