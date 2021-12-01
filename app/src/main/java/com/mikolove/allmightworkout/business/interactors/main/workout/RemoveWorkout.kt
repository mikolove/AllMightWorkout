package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RemoveWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource
) {

   fun execute(
        workout : Workout,
    ) : Flow<DataState<Int>?> = flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.removeWorkout(workout.idWorkout)
        }

        val response = object : CacheResponseHandler<Int, Int>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                return if( resultObj > 0){
                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("RemoveWorkout.Success")
                            .title("")
                            .description(DELETE_WORKOUT_SUCCESS)
                            .messageType(MessageType.Success)
                            .uiComponentType(UIComponentType.Toast),
                        data = resultObj)

                }else{
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("RemoveWorkout.Error")
                            .title("")
                            .description(DELETE_WORKOUT_FAILED)
                            .messageType(MessageType.Error)
                            .uiComponentType(UIComponentType.Toast),
                        )
                }
            }
        }.getResult()

        emit(response)

        //Update network
        updateNetwork(response?.message?.description, workout)
    }

    private suspend fun updateNetwork(cacheResponse : String?, deletedWorkout : Workout) {
        if(cacheResponse.equals(DELETE_WORKOUT_SUCCESS)){
            safeApiCall(IO){
                workoutNetworkDataSource.removeWorkout(deletedWorkout.idWorkout)
            }

            safeApiCall(IO){
                workoutNetworkDataSource.insertDeleteWorkout(deletedWorkout)
            }
        }
    }

    companion object{
        val DELETE_WORKOUT_SUCCESS = "Successfully deleted workout"
        val DELETE_WORKOUT_FAILED = "Failed deleting workout"
        val DELETE_WORKOUT_ARE_YOU_SURE = "Are you sure to delete this workout ?"
    }
}