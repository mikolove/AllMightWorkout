package com.mikolove.core.interactors.common

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExerciseById(
    val exerciseCacheDataSource: ExerciseCacheDataSource
) {

        fun execute(
            idExercise : String
        ): Flow<DataState<Exercise>?> = flow {

            emit(DataState.loading())

            val cacheResult = safeCacheCall(IO){
                exerciseCacheDataSource.getExerciseById(idExercise)
            }

            val response  = object : CacheResponseHandler<Exercise, Exercise>(
                response = cacheResult
            ){
                override suspend fun handleSuccess(resultObj: Exercise): DataState<Exercise> {

                    return DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("GetExerciseById.Success")
                            .title("Get exercise by id")
                            .description(GET_EXERCISE_BY_ID_SUCCESS)
                            .messageType(MessageType.Success)
                            .uiComponentType(UIComponentType.None),
                        data = resultObj,
                    )
                }
            }.getResult()

            emit(response)
        }

    companion object{
        val GET_EXERCISE_BY_ID_SUCCESS = "Successfully retrieved exercise by id."
    }
}