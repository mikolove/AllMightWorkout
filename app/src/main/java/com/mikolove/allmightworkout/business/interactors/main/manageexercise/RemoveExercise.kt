package com.mikolove.allmightworkout.business.interactors.main.manageexercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveExercise<ViewState>(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
){

    fun removeExercise(
        exercise : Exercise,
        stateEvent : StateEvent
    ) : Flow<DataState<ManageExerciseViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.removeExerciseById(exercise.idExercise)
        }

        val response = object : CacheResponseHandler<ManageExerciseViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ManageExerciseViewState>? {
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

        updateNetwork(response?.stateMessage?.response?.message, exercise)

    }

    suspend fun updateNetwork(cacheResponse : String?, deletedExercise : Exercise){
        if(cacheResponse.equals(DELETE_EXERCISE_SUCCESS)){
           safeApiCall(IO){
               exerciseNetworkDataSource.removeExerciseById(deletedExercise.idExercise)
           }

            safeApiCall(IO){
                exerciseNetworkDataSource.insertExercise(deletedExercise)
            }
        }
    }
    companion object{
        val DELETE_EXERCISE_SUCCESS = "Successfully deleted exercise."
        val DELETE_EXERCISE_FAILED = "Failed deleting exercise."
        val DELETE_EXERCISE_ARE_YOU_SURE = "Are you sure to delete this ?"
    }
}