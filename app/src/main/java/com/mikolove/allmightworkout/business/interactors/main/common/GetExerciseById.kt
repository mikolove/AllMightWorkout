package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
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
                override suspend fun handleSuccess(resultObj: Exercise): DataState<Exercise>? {

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