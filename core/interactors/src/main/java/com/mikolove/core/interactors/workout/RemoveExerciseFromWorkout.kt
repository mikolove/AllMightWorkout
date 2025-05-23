package com.mikolove.core.interactors.workout

import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveExerciseFromWorkout(
    private val workoutCacheDataSource : WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
) {

 /*   fun execute(
        idExercise : String,
        idWorkout: String,
    ) : Flow<DataState<Int>?> = flow {

        //Update exerciseIds
        val updatedExerciseIdsDate = dateUtil.getCurrentTimestamp()
        val isExerciseIdsUpdated = updateExerciseIdsUpdatedAt(idWorkout, updatedExerciseIdsDate )

        //If it fails stop the process
        if(isExerciseIdsUpdated == 0){
            emit(DataState<Int>(
                message = GenericMessageInfo.Builder()
                    .id("RemoveExerciseFromWorkout.UpdateFail")
                    .title("RemoveExerciseFromWorkout update fail")
                    .description(REMOVE_WORKOUT_EXERCISE_UPDATE_FAILED)
                    .uiComponentType(UIComponentType.Toast)
                    .messageType(MessageType.Error)
            ))
        //If not continue
        }else{

            val cacheResult = safeCacheCall(IO){
                exerciseCacheDataSource.removeExerciseFromWorkout(idWorkout,idExercise)
            }

            val response = object : CacheResponseHandler<Int, Int>(
                response = cacheResult
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                    return if(resultObj>0){
                        DataState<Int>(
                            message = GenericMessageInfo.Builder()
                                .id("RemoveExerciseFromWorkout.Success")
                                .title("RemoveExerciseFromWorkout success")
                                .description(REMOVE_WORKOUT_EXERCISE_SUCCESS)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = resultObj)
                    }else{

                        DataState<Int>(
                            message = GenericMessageInfo.Builder()
                                .id("RemoveExerciseFromWorkout.Failed")
                                .title("RemoveExerciseFromWorkout failed")
                                .description(REMOVE_WORKOUT_EXERCISE_FAILED)
                                .uiComponentType(UIComponentType.Toast)
                                .messageType(MessageType.Error))
                    }
                }
            }.getResult()

            emit(response)

            updateNetwork(response.message?.description, idWorkout,idExercise,updatedExerciseIdsDate)

        }
    }

    private suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, dateUpdated: String) : Int{

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout, dateUpdated)
        }

        val cacheResponse = object : CacheResponseHandler<Int, Int>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                return DataState.data(
                    message = null,
                    data = resultObj
                )
            }
        }.getResult()

        return cacheResponse.data ?: 0
    }

    private suspend fun updateNetwork(message : String?, idWorkout: String, idExercise: String, dateUpdated : String){
        if(message.equals(REMOVE_WORKOUT_EXERCISE_SUCCESS)) {
            safeApiCall(IO){
                exerciseNetworkDataSource.removeExerciseFromWorkout(idWorkout,idExercise)
            }
            safeApiCall(IO){
                workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,dateUpdated)
            }
        }
    }

    companion object{
        val REMOVE_WORKOUT_EXERCISE_SUCCESS  = "Successfully deleted added exercise to workout."
        val REMOVE_WORKOUT_EXERCISE_ARE_YOU_SURE  = "Are you sure to remove this exercise from workout ?"
        val REMOVE_WORKOUT_EXERCISE_UPDATE_FAILED  = "Failed to update last insert exerciseIds."
        val REMOVE_WORKOUT_EXERCISE_FAILED  = "Failed deleting exercise from workout."
    }*/
}