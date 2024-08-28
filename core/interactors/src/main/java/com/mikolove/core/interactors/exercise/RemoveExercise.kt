package com.mikolove.core.interactors.exercise

import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource

class RemoveExercise<ViewState>(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
){

/*
    fun removeExercise(
        exercise : Exercise,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.removeExerciseById(exercise.idExercise)
        }

        val response = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {
                return if(resultObj>0){

                    DataState.data(
                        response = Response(
                            message = DELETE_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent)

                } else {

                    DataState.data(
                        response = Response(
                            message = DELETE_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)
                }
            }
        }.getResult()

        emit(response)

        updateNetwork(response?.message?.response?.message, exercise)

    }

    suspend fun updateNetwork(cacheResponse : String?, deletedExercise : Exercise){
        if(cacheResponse.equals(DELETE_EXERCISE_SUCCESS)){
           safeApiCall(IO){
               exerciseNetworkDataSource.removeExerciseById(deletedExercise.idExercise)
           }

            safeApiCall(IO){
                exerciseNetworkDataSource.insertDeletedExercise(deletedExercise)
            }
        }
    }
*/
    companion object{
        val DELETE_EXERCISE_SUCCESS = "Successfully deleted exercise."
        val DELETE_EXERCISE_FAILED = "Failed deleting exercise."
        val DELETE_EXERCISE_ARE_YOU_SURE = "Are you sure to delete this ?"
    }
}