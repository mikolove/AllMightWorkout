package com.mikolove.core.interactors.exercise

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource

import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveMultipleExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    // set true if an error occurs when deleting any of the exercises from cache
  /*  private var onDeleteError: Boolean = false

    fun execute(
        exercises : List<Exercise>,
    ): Flow<DataState<Int>?> = flow {

        val successfulDeletes : ArrayList<Exercise> = ArrayList()

        for(exercise in exercises){

            val cacheResult = safeCacheCall(IO) {
                exerciseCacheDataSource.removeExerciseById(exercise.idExercise)
            }

            val response = object : CacheResponseHandler<Int, Int>(
                response = cacheResult
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                    if (resultObj < 0) { // error
                        onDeleteError = true
                    } else { // success
                        successfulDeletes.add(exercise)
                    }
                    return DataState.data(
                        message = null,
                        data = resultObj
                    )
                }
            }.getResult()

            //Check for random errors
            if (response.message?.messageType is MessageType.Error) {
                onDeleteError = true
            }

        }

        if(onDeleteError){
            emit(
                DataState<Int>(
                    message = GenericMessageInfo.Builder()
                        .id("RemoveMultipleExercises.Error")
                        .title("")
                        .description(DELETE_EXERCISES_ERRORS)
                        .uiComponentType(UIComponentType.Toast)
                        .messageType(MessageType.Error))
            )
        }else{
            emit(
                DataState<Int>(
                    message = GenericMessageInfo.Builder()
                        .id("RemoveMultipleExercises.Success")
                        .title("")
                        .description(DELETE_EXERCISES_SUCCESS)
                        .uiComponentType(UIComponentType.Toast)
                        .messageType(MessageType.Success))
            )
        }

        updateNetwork(successfulDeletes)
    }


    private suspend fun updateNetwork(successfulDeletes: ArrayList<Exercise>){
        for (exercise in successfulDeletes){
            safeApiCall(IO){
                exerciseNetworkDataSource.removeExerciseById(exercise.idExercise)
            }

            safeApiCall(IO){
                exerciseNetworkDataSource.insertDeletedExercises(successfulDeletes)
            }
        }
    }


    companion object{
        val DELETE_EXERCISES_SUCCESS = "Successfully deleted exercises."
        val DELETE_EXERCISES_ERRORS = "Not all the exercises were deleted. Errors occurs."
        val DELETE_EXERCISES_YOU_MUST_SELECT ="You haven't selected any exercises to delete."
        val DELETE_EXERCISES_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }*/

}