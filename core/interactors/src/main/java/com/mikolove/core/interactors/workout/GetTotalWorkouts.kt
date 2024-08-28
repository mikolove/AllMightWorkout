package com.mikolove.core.interactors.workout

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource,

    ) {

    fun execute(
        idUser : String
    ) : Flow<DataState<Int>?> = flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getTotalWorkout(idUser)
        }

        val response = object : CacheResponseHandler<Int, Int>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("GetTotalWorkouts.Success")
                        .title("")
                        .description(GET_TOTAL_WORKOUT_SUCCESS)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = resultObj
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_TOTAL_WORKOUT_SUCCESS = "Successfully retrieved the number of workouts from the cache."
        val GET_TOTAL_WORKOUT_FAILED = "Failed to retrieved the number of workouts from the cache."
    }
}