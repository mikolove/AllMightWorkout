package com.mikolove.core.interactors.workout

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class
InsertWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val workoutFactory: WorkoutFactory
) {

    fun execute(
        idWorkout : String? = null,
        name : String,
        idUser : String
    ) : Flow<DataState<String>?> = flow{

        val newWorkout = workoutFactory.createWorkout(
            idWorkout = idWorkout?: UUID.randomUUID().toString(),
            name = name,
            exercises = null,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.insertWorkout(newWorkout,idUser)
        }

        val cacheResponse = object : CacheResponseHandler<String, Long>(
            response = cacheResult,
        ){

            override suspend fun handleSuccess(resultObj: Long): DataState<String> {
                return if (resultObj > 0) {

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("InsertWorkout.Success")
                            .title("")
                            .description(INSERT_WORKOUT_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = newWorkout.idWorkout)

                } else {

                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("InsertWorkout.Error")
                            .title("")
                            .description(INSERT_WORKOUT_FAILED)
                            .uiComponentType(UIComponentType.Toast)
                            .messageType(MessageType.Error))
                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse.message?.description, newWorkout)
    }


    private suspend fun updateNetwork(cacheResponse : String?, newWorkout : Workout) {
        if(cacheResponse.equals(INSERT_WORKOUT_SUCCESS)){

            safeApiCall(IO){
                workoutNetworkDataSource.insertWorkout(newWorkout)
            }
        }
    }

    companion object{
        val INSERT_WORKOUT_SUCCESS = "Successfully inserted new workout."
        val INSERT_WORKOUT_FAILED  = "Failed inserting new workout."
    }
}