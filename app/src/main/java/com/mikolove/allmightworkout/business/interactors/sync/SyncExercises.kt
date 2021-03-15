package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SyncExercises(
    private val dateFormat: SimpleDateFormat,
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

    suspend fun syncExercises(){
        val cachedExercises = getCachedExercises()

        val cachedExerciseSets = HashMap<String,List<ExerciseSet>>()

        syncExercisesCacheAndNetwork(ArrayList(cachedExercises),cachedExerciseSets)
    }

    private suspend fun getCachedExercises() : List<Exercise>{

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.getExercises("","",1)
        }

        val response = object : CacheResponseHandler<List<Exercise>,List<Exercise>>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<List<Exercise>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    private suspend fun syncExercisesCacheAndNetwork(
        cachedExercises : ArrayList<Exercise>,
        cachedExerciseSets : HashMap<String,List<ExerciseSet>>
    ) = withContext(IO){

        //Get all network exercises
        val networkResult = safeApiCall(IO){
            exerciseNetworkDataSource.getExercises()
        }

        val networkResponse = object : ApiResponseHandler<List<Exercise>,List<Exercise>>(
            response = networkResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<List<Exercise>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val networkExercises = networkResponse?.data ?: ArrayList()

        //For each exercises in network
        // if exist update it if needed
        // if not insert it
        //val job = launch {
            for(networkExercise in networkExercises){

                //It exist try update it
                exerciseCacheDataSource.getExerciseById(networkExercise.idExercise)?.also { cachedExercise ->

                    cachedExercises.remove(cachedExercise)
                    updateExerciseIfNeeded(networkExercise,cachedExercise)

                    //get all cachedSets for exercise
                    val cachedSets = ArrayList(exerciseSetCacheDataSource.getExerciseSetByIdExercise(cachedExercise.idExercise))

                    for(networkExerciseSet in networkExercise.sets){

                        //If exist try updatde it
                        exerciseSetCacheDataSource.getExerciseSetById(networkExerciseSet.idExerciseSet,networkExercise.idExercise)?.also{
                                cachedExerciseSet ->
                            cachedSets.remove(cachedExerciseSet)
                            updateExerciseSetIfNeeded(networkExerciseSet,cachedExerciseSet,cachedExercise.idExercise)

                            //if not exist insert it
                        }?:insertExerciseSetToCache(networkExerciseSet,cachedExercise.idExercise)
                    }

                    cachedExerciseSets.put(cachedExercise.idExercise,cachedSets)

                    //If not exist insert it
                }?: insertExerciseToCache(networkExercise)
            }
        //}

        //wait for it to complete
        //job.join()

        //Insert exercise in cache not found in network
        for(cachedExerciseNotFound in cachedExercises){
            printLogD("SyncExercises","Insert exercise into network ${cachedExerciseNotFound.idExercise}")
            safeApiCall(IO){
                exerciseNetworkDataSource.insertExercise(cachedExerciseNotFound)
            }
        }

        //Insert exerciseSet in cache not found in network
        for( (idExercise, cachedExerciseSetNotFound) in cachedExerciseSets){
            cachedExerciseSetNotFound.forEach { set->
                printLogD("SyncExercises","Insert exercise set into network ${set.idExerciseSet}")
                safeApiCall(IO){
                    exerciseSetNetworkDataSource.insertExerciseSet(set,idExercise)
                }
            }
        }

    }

    private suspend fun updateExerciseIfNeeded(networkExercise : Exercise, cacheExercise : Exercise){

        val networkUpdatedAt = dateFormat.parse(networkExercise.updatedAt)
        val cacheUpdatedAt = dateFormat.parse(cacheExercise.updatedAt)

        //If network has newest data
        if(networkUpdatedAt != cacheUpdatedAt) {
            printLogD("SyncExercises","Try to update exercise")

            //If network has newest data
            if(networkUpdatedAt.after(cacheUpdatedAt)){
                printLogD("SyncExercises","Exercises in cache updated ${cacheExercise.idExercise}")
                exerciseCacheDataSource.updateExercise(
                    networkExercise.idExercise,
                    networkExercise.name,
                    networkExercise.bodyPart,
                    networkExercise.isActive,
                    networkExercise.exerciseType.name,
                    networkExercise.updatedAt
                )

            }
            //If cache has newest data
            if(networkUpdatedAt.before(cacheUpdatedAt)) {
                printLogD("SyncExercises", "Exercises in network updated ${cacheExercise.idExercise}")
                exerciseNetworkDataSource.updateExercise(
                    cacheExercise
                )
            }
        }
    }

    private suspend fun updateExerciseSetIfNeeded(networkExerciseSet : ExerciseSet, cacheExerciseSet: ExerciseSet, idExercise : String) {

        val networkUpdatedAt = dateFormat.parse(networkExerciseSet.updatedAt)
        val cacheUpdatedAt = dateFormat.parse(cacheExerciseSet.updatedAt)

        if(networkUpdatedAt != cacheUpdatedAt) {
            printLogD("SyncExercises", "Try to update exercise set")

            //If network has newest data
            if (networkUpdatedAt.after(cacheUpdatedAt)) {
                printLogD(
                    "SyncExercises",
                    "Exercise set in cache updated ${cacheExerciseSet.idExerciseSet}"
                )
                exerciseSetCacheDataSource.updateExerciseSet(
                    networkExerciseSet.idExerciseSet,
                    networkExerciseSet.reps,
                    networkExerciseSet.weight,
                    networkExerciseSet.time,
                    networkExerciseSet.restTime,
                    networkExerciseSet.updatedAt,
                    idExercise
                )
            }
            //If cache has newest data
            if (networkUpdatedAt.before(cacheUpdatedAt)) {
                printLogD(
                    "SyncExercises",
                    "Exercise set in network updated ${cacheExerciseSet.idExerciseSet}"
                )
                exerciseSetNetworkDataSource.updateExerciseSet(
                    cacheExerciseSet,
                    idExercise
                )
            }
        }
    }

    private suspend fun insertExerciseToCache(exercise : Exercise){
        printLogD("SyncExercises","insertExerciseToCache ${exercise.idExercise}")
        exerciseCacheDataSource.insertExercise(exercise)

        exercise.sets?.forEach { exerciseSet ->
            insertExerciseSetToCache(exerciseSet,exercise.idExercise)
        }
    }

    private suspend fun insertExerciseSetToCache(exerciseSet : ExerciseSet, idExercise: String){
        printLogD("SyncExercises","insertExerciseSetToCache ${idExercise} ${exerciseSet.idExerciseSet}")
        exerciseSetCacheDataSource.insertExerciseSet(exerciseSet,idExercise)
    }
}