package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutById(
    val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun execute(
        idWorkout : String,
    ) : Flow<DataState<Workout>?> =  flow {

        emit(DataState.loading())

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkoutById(idWorkout)
        }

        val response = object : CacheResponseHandler<Workout, Workout>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: Workout): DataState<Workout> {

                return DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("GetWorkoutById.Success")
                        .title("")
                        .description(GET_WORKOUT_BY_ID_SUCCESS)
                        .messageType(MessageType.Success)
                        .uiComponentType(UIComponentType.None),
                    data = resultObj)
            }
        }.getResult()

        /*if(response?.data == null){
            emit(DataState.error(
                message = GenericMessageInfo.Builder()
                    .id("GetWorkoutById.Error")
                    .title("")
                    .description(GET_WORKOUT_BY_ID_FAILED)
                    .messageType(MessageType.Error)
                    .uiComponentType(UIComponentType.Toast))
            )
        }else{}*/
        emit(response)

    }

    companion object{
        val GET_WORKOUT_BY_ID_SUCCESS = "Successfully retrieved workout by id."
        val GET_WORKOUT_BY_ID_FAILED  = "Failed retrieving workout by id."
    }

}