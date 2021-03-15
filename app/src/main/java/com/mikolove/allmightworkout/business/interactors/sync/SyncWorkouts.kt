package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class SyncWorkouts(
    private val dateFormat: SimpleDateFormat,
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    suspend fun syncWorkouts(){
        val cachedWorkouts = ArrayList(getCachedWorkouts())

        syncCacheAndNetwork(cachedWorkouts)
    }

    suspend fun getCachedWorkouts() : List<Workout>{

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkouts("","",1)
        }

        val response = object :CacheResponseHandler<List<Workout>,List<Workout>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>>? {
                return DataState.data(
                    response =  null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    private suspend fun syncCacheAndNetwork(cachedWorkouts : ArrayList<Workout>) = withContext(IO){

        //Get all network workouts
        val apiResult = safeApiCall(IO){
            workoutNetworkDataSource.getWorkouts()
        }

        val apiResponse = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response =  apiResult,
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

        val networkWorkouts = apiResponse?.data ?: ArrayList()

        //val job = launch {
            //Compare workout in network with cache and proceed update if needed
            for(networkWorkout in networkWorkouts){

                workoutCacheDataSource.getWorkoutById(networkWorkout.idWorkout)?.also { cacheWorkout ->
                    //Update core workout
                    cachedWorkouts.remove(cacheWorkout)
                    updateWorkoutIfNeeded(networkWorkout,cacheWorkout)

                    /*
                        TODO : Update link between exercises and workout need to figure how will it work with firebase and room relational model
                        cacheWorkout.exercises?.forEach{ cacheExercise ->
                       }
                    */

                }?: insertWorkoutInCache(networkWorkout)
            }
        //}

        //job.join()

        //if workout from network not found in cache insert it in network
        for(notFoundWorkout in cachedWorkouts){
            printLogD("SyncWorkouts","Insert into network")
            workoutNetworkDataSource.insertWorkout(notFoundWorkout)
        }
    }

    private suspend fun insertWorkoutInCache(networkWorkout : Workout) {
        printLogD("SyncWorkouts","Insert into cache")
        workoutCacheDataSource.insertWorkout(networkWorkout)
    }

    private suspend fun updateWorkoutIfNeeded(networkWorkout : Workout, cacheWorkout : Workout){

        val networkUpdatedAt = dateFormat.parse(networkWorkout.updatedAt)
        val cacheUpdatedAt = dateFormat.parse(cacheWorkout.updatedAt)

        //If network has newest data
        if(networkUpdatedAt != cacheUpdatedAt) {
            printLogD("SyncWorkouts","Try to update workout")

            //If network has newest data
            if(networkUpdatedAt.after(cacheUpdatedAt)){
                printLogD("SyncWorkouts","Workout in cache updated ${cacheWorkout.idWorkout}")
                workoutCacheDataSource.updateWorkout(
                    networkWorkout.idWorkout,
                    networkWorkout.name,
                    networkWorkout.isActive,
                    networkWorkout.updatedAt
                )

            }
            //If cache has newest data
            if(networkUpdatedAt.before(cacheUpdatedAt)) {
                printLogD("SyncWorkouts","Workout in network updated ${cacheWorkout.idWorkout}")
                workoutNetworkDataSource.updateWorkout(
                    cacheWorkout
                )
            }
        }
    }
}