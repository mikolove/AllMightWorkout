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

class UpdateExercise(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
) {

    fun updateExercise(
        exercise : Exercise,
        stateEvent : StateEvent
    ): Flow<DataState<ManageExerciseViewState>?> = flow{

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.updateExercise(
                primaryKey = exercise.idExercise,
                name = exercise.name,
                bodyPart = exercise.bodyPart,
                isActive = exercise.isActive,
                exerciseType = exercise.exerciseType.type,
                updatedAt = exercise.updatedAt
            )
        }

        val response = object  : CacheResponseHandler<ManageExerciseViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ManageExerciseViewState>? {
                return if(resultObj>0){
                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)

        updateNetwork(response?.stateMessage?.response?.message,exercise)
    }

    private suspend fun updateNetwork(response: String?, exercise: Exercise){
        if(response.equals(UPDATE_EXERCISE_SUCCESS)){
            safeApiCall(IO){
                exerciseNetworkDataSource.updateExercise(exercise)
            }
        }

    }
    companion object{
        val UPDATE_EXERCISE_SUCCESS = "Successfully updated exercise."
        val UPDATE_EXERCISE_FAILED  = "Failed updated exercise."
    }

}