package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.ApiResponseHandler
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class SyncWorkoutExercises(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val dateUtil: DateUtil
) {


    suspend fun syncWorkoutExercises() {

        val cachedWorkouts = ArrayList(getCachedWorkouts())

        syncCacheAndNetwork(cachedWorkouts)
    }

    private suspend fun getCachedWorkouts(): List<Workout> {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            workoutCacheDataSource.getWorkouts("", "", 1)
        }

        val response = object : CacheResponseHandler<List<Workout>, List<Workout>>(
            response = cacheResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    private suspend fun syncCacheAndNetwork(cachedWorkouts: ArrayList<Workout>) = withContext(IO) {

        printLogD("SyncWorkoutExercises","Start")
        //Get all network workouts
        val apiResult = safeApiCall(IO) {
            workoutNetworkDataSource.getWorkouts()
        }

        val apiResponse = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response = apiResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        val networkWorkouts = apiResponse?.data ?: ArrayList()

        for (networkWorkout in networkWorkouts) {

            val cacheWorkout = cachedWorkouts.find { it.idWorkout == networkWorkout.idWorkout }

            val networkUpdatedAt = networkWorkout.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }
            val cacheUpdatedAt = cacheWorkout?.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }

/*            printLogD("SyncWorkoutExercises","For network workout ${networkWorkout.idWorkout}")
            printLogD("SyncWorkoutExercises","And cache workout  ${cacheWorkout?.idWorkout}")
            printLogD("SyncWorkoutExercises","networkUpdatedAT = ${networkUpdatedAt}")
            printLogD("SyncWorkoutExercises","cacheUpdatedAt ${cacheUpdatedAt}")*/

            //If one or the other is not null
            if (networkUpdatedAt != null || cacheUpdatedAt != null) {

               // printLogD("SyncWorkoutExercises","networkUpdatedAt != null || cacheUpdatedAt != null")

                if (cacheUpdatedAt == null && networkUpdatedAt != null) {
                   // printLogD("SyncWorkoutExercises","cacheUpdatedAt == null && networkUpdatedAt != null")

                    //Update cache with network
                    updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout?.exercises)

                } else if (cacheUpdatedAt != null && networkUpdatedAt == null) {
                    //printLogD("SyncWorkoutExercises","cacheUpdatedAt != null && networkUpdatedAt == null")

                    //Update network with cache
                    updateWorkoutExerciseInNetworkWithCache(
                        cacheWorkout.idWorkout,networkWorkout.exercises, cacheWorkout?.exercises)

                } else if (cacheUpdatedAt != null && networkUpdatedAt != null) {

                    //printLogD("SyncWorkoutExercises","cacheUpdatedAt != null && networkUpdatedAt != null")

                    //Compare them
                    if (networkUpdatedAt != cacheUpdatedAt) {

                        //printLogD("SyncWorkoutExercises","networkUpdatedAt != cacheUpdatedAt")
                        if (networkUpdatedAt.after(cacheUpdatedAt)) {
                            //printLogD("SyncWorkoutExercises","networkUpdatedAt.after(cacheUpdatedAt)")

                            //Update cache with network
                            updateWorkoutExerciseInCacheWithNetwork(
                                networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout?.exercises)

                        }

                        if (networkUpdatedAt.before(cacheUpdatedAt)) {
                            //printLogD("SyncWorkoutExercises","networkUpdatedAt.before(cacheUpdatedAt)")

                            //Update network with cache
                            updateWorkoutExerciseInNetworkWithCache(cacheWorkout.idWorkout, networkWorkout.exercises, cacheWorkout?.exercises)

                        }
                    }
                }

            //First if end
            }
         //Foor loop end
        }
    }

    private suspend fun updateWorkoutExerciseInCacheWithNetwork(
        idWorkout: String,
        networkExercises: List<Exercise>?,
        cacheExercises: List<Exercise>?
    ) {
        printLogD("SyncWorkoutExercises","try updateWorkoutExerciseInCacheWithNetwork")
        //Delete All and Insert new data
        cacheExercises?.forEach {  exerciseCache ->
            printLogD("SyncWorkoutExercises","remove in cache")
            exerciseCacheDataSource.removeExerciseFromWorkout(idWorkout, exerciseCache.idExercise)
        }
        networkExercises?.forEach { exerciseNetwork ->
            printLogD("SyncWorkoutExercises","add in cache")
            exerciseCacheDataSource.addExerciseToWorkout(idWorkout, exerciseNetwork.idExercise)
        }

        if(cacheExercises.isNullOrEmpty()){
            printLogD("SyncWorkoutExercises","Update exerciseIdsUpdatedAt to null")
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout,null)
            workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,null)
        }else{
            val updatedAt = dateUtil.getCurrentTimestamp()
            printLogD("SyncWorkoutExercises","Update exerciseIdsUpdatedAt to ${updatedAt}")
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout,updatedAt)
            workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,updatedAt)
        }
    }

    private suspend fun updateWorkoutExerciseInNetworkWithCache(
        idWorkout: String,
        networkExercises: List<Exercise>?,
        cacheExercises: List<Exercise>?
    ) {

        printLogD("SyncWorkoutExercises","try updateWorkoutExerciseInNetworkWithCache")
        //Delete All and Insert new data
        networkExercises?.forEach { exerciseNetwork ->
            printLogD("SyncWorkoutExercises","remove in network")
            exerciseNetworkDataSource.removeExerciseFromWorkout(idWorkout,exerciseNetwork.idExercise)
        }
        cacheExercises?.forEach {  exerciseCache ->
            printLogD("SyncWorkoutExercises","add in network")
            exerciseNetworkDataSource.addExerciseToWorkout(idWorkout, exerciseCache.idExercise)
        }

        if(networkExercises.isNullOrEmpty()){
            printLogD("SyncWorkoutExercises","Update exerciseIdsUpdatedAt to null")
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout,null)
            workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,null)
        }else{
            val updatedAt = dateUtil.getCurrentTimestamp()
            printLogD("SyncWorkoutExercises","Update exerciseIdsUpdatedAt to ${updatedAt}")
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout,updatedAt)
            workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,updatedAt)
        }

    }

}