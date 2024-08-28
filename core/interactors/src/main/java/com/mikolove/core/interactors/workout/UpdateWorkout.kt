package com.mikolove.core.interactors.workout

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.data.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.core.domain.workout.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class UpdateWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
) {

    fun execute(
        workout : Workout,
    ): Flow<DataState<Int>?> = flow {

        emit(DataState.loading())

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.updateWorkout(
                workout.idWorkout,
                workout.name,
                workout.isActive,
                workout.updatedAt
            )
        }

        val response = object : CacheResponseHandler<Int, Int>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                return if(resultObj > 0){

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("UpdateWorkout.Success")
                            .title("")
                            .description(UPDATE_WORKOUT_SUCCESS)
                            .messageType(MessageType.Success)
                            .uiComponentType(UIComponentType.None),
                        data = resultObj)
                }
                else {
                    DataState.error(
                        message = GenericMessageInfo.Builder()
                            .id("UpdateWorkout.Error")
                            .title("")
                            .description(UPDATE_WORKOUT_FAILED)
                            .messageType(MessageType.Error)
                            .uiComponentType(UIComponentType.None))
                }

            }
        }.getResult()

        emit(response)

        updateNetwork(response.message?.description, workout)

    }

    private suspend fun updateNetwork(response: String?, workout: Workout){
        if(response.equals(UPDATE_WORKOUT_SUCCESS)){
            safeApiCall(IO){
                workoutNetworkDataSource.updateWorkout(workout)
            }
        }

    }

    companion object{
        val UPDATE_WORKOUT_SUCCESS = "Successfully updated workout."
        val UPDATE_WORKOUT_FAILED  = "Failed updated workout."
    }
}