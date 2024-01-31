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
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
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

    /*
        LOGIC HERE IS NOT HAVE TO REDO THIS
        IF LAST UPDATE IS NULL WHEN SYNC ANOTHER DEVICE TRYING TO SYNC BEFORE A MODIFICATION IS DONE
        THE NEW DEVICE WILL NEVER GET THE LINKED EXERCISES

     */

    suspend fun execute(
        idUser : String,
    ) : DataState<SyncState>{

        val cachedWorkouts = getCachedWorkouts(idUser)

        val apiResult = safeApiCall(IO) {
            workoutNetworkDataSource.getWorkouts()
        }

        val apiResponse = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response = apiResult,
        ) {
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        if(apiResponse.message?.messageType == MessageType.Error) {

            return DataState.data(
                message = GenericMessageInfo.Builder()
                    .id("SyncWorkoutExercises.GlobalError")
                    .title(SyncEverything.SYNC_GERROR_TITLE)
                    .description(SyncEverything.SYNC_GERROR_DESCRIPTION)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Dialog),
                data = SyncState.FAILURE
            )

        }else{
            try{

                val networkWorkouts = apiResponse.data ?: ArrayList()

                for (networkWorkout in networkWorkouts) {

                    val cacheWorkout = cachedWorkouts.find { it.idWorkout == networkWorkout.idWorkout }

                    val networkUpdatedAt = networkWorkout.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }
                    val cacheUpdatedAt = cacheWorkout?.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }

                    //If one or the other is not null
                    if (networkUpdatedAt != null || cacheUpdatedAt != null) {

                        if (cacheUpdatedAt == null && networkUpdatedAt != null) {

                            //Update cache with network
                            updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout?.exercises)

                        } else if (cacheUpdatedAt != null && networkUpdatedAt == null) {

                            //Update network with cache
                            updateWorkoutExerciseInNetworkWithCache(cacheWorkout.idWorkout,networkWorkout.exercises, cacheWorkout.exercises)

                        } else if (cacheUpdatedAt != null && networkUpdatedAt != null) {

                            //Compare them
                            if (networkUpdatedAt != cacheUpdatedAt) {

                                if (networkUpdatedAt.after(cacheUpdatedAt)) {

                                    //Update cache with network
                                    updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)
                                }

                                if (networkUpdatedAt.before(cacheUpdatedAt)) {

                                    //Update network with cache
                                    updateWorkoutExerciseInNetworkWithCache(cacheWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)
                                }

                                //Update cache with network when this is a new installation
                            }else if(networkUpdatedAt.equals(cacheUpdatedAt)){

                                val cacheExerciseSize = cacheWorkout.exercises?.size ?: 0
                                val networkExerciseSize = networkWorkout.exercises?.size ?:0
                                if(cacheExerciseSize == 0 && networkExerciseSize >0){
                                    updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)
                                }
                            }
                        }
                    }
                }

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkoutExercises.Success")
                        .title(SYNC_WE_TITLE)
                        .description(SYNC_WE_DESCRIPTION)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = SyncState.SUCCESS
                )

            }catch (e : Exception){

                printLogD("SyncWorkoutExercises","Exception : ${e}")
                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("SyncWorkoutExercises.Error")
                        .title(SYNC_WE_ERROR_TITLE)
                        .description(SYNC_WE_ERROR_DESCRIPTION)
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

        val response = object :CacheResponseHandler<List<Workout>,List<Workout>>(
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

    private suspend fun syncCacheAndNetwork(cachedWorkouts: ArrayList<Workout>) = withContext(IO) {

        //Get all network workouts
        val apiResult = safeApiCall(IO) {
            workoutNetworkDataSource.getWorkouts()
        }

        val apiResponse = object : ApiResponseHandler<List<Workout>, List<Workout>>(
            response = apiResult,
        ) {
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<List<Workout>> {
                return DataState.data(
                    message = null,
                    data = resultObj,
                )
            }
        }.getResult()

        val networkWorkouts = apiResponse.data ?: ArrayList()

        for (networkWorkout in networkWorkouts) {

            val cacheWorkout = cachedWorkouts.find { it.idWorkout == networkWorkout.idWorkout }

            val networkUpdatedAt = networkWorkout.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }
            val cacheUpdatedAt = cacheWorkout?.exerciseIdsUpdatedAt?.let { dateUtil.convertStringDateToDate(it) }

            printLogD("SyncWorkoutExercises","For network workout ${networkWorkout.idWorkout}")
            printLogD("SyncWorkoutExercises","And cache workout  ${cacheWorkout?.idWorkout}")
            printLogD("SyncWorkoutExercises","networkUpdatedAT = ${networkUpdatedAt}")
            printLogD("SyncWorkoutExercises","cacheUpdatedAt ${cacheUpdatedAt}")

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
                        cacheWorkout.idWorkout,networkWorkout.exercises, cacheWorkout.exercises)

                } else if (cacheUpdatedAt != null && networkUpdatedAt != null) {

                    //printLogD("SyncWorkoutExercises","cacheUpdatedAt != null && networkUpdatedAt != null")

                    //Compare them
                    if (networkUpdatedAt != cacheUpdatedAt) {

                        //printLogD("SyncWorkoutExercises","networkUpdatedAt != cacheUpdatedAt")
                        if (networkUpdatedAt.after(cacheUpdatedAt)) {
                            //printLogD("SyncWorkoutExercises","networkUpdatedAt.after(cacheUpdatedAt)")

                            //Update cache with network
                            updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)

                        }

                        if (networkUpdatedAt.before(cacheUpdatedAt)) {
                            //printLogD("SyncWorkoutExercises","networkUpdatedAt.before(cacheUpdatedAt)")

                            //Update network with cache
                            updateWorkoutExerciseInNetworkWithCache(cacheWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)

                        }

                        //Update cache with network when this is a new installation
                    }else if(networkUpdatedAt.equals(cacheUpdatedAt)){

                        printLogD("SyncWorkoutExercises","networkUpdatedAt.equals(cacheUpdatedAt)")

                        val cacheExerciseSize = cacheWorkout.exercises?.size ?: 0
                        val networkExerciseSize = networkWorkout.exercises?.size ?:0
                        if(cacheExerciseSize == 0 && networkExerciseSize >0){
                            printLogD("SyncWorkoutExercises","cache size ${cacheExerciseSize} network size ${networkExerciseSize}")
                            updateWorkoutExerciseInCacheWithNetwork(networkWorkout.idWorkout, networkWorkout.exercises, cacheWorkout.exercises)
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

    companion object{
        val SYNC_WE_TITLE = "Sync success"
        val SYNC_WE_DESCRIPTION = "Successfully sync workouts and exercises"

        val SYNC_WE_ERROR_TITLE = "Sync error"
        val SYNC_WE_ERROR_DESCRIPTION = "Failed retrieving workouts. Check internet or try again later."

    }

}