package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveExerciseFromWorkout(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    fun removeExerciseFromWorkout(
        idExercise : String,
        idWorkout: String,
        stateEvent : StateEvent
    ) : Flow<DataState<ManageWorkoutViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.removeExerciseFromWorkout(idWorkout,idExercise)
        }

        val response = object : CacheResponseHandler<ManageWorkoutViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ManageWorkoutViewState>? {
                return if(resultObj>0){
                    DataState.data(
                        response = Response(
                            message = REMOVE_WORKOUT_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = REMOVE_WORKOUT_EXERCISE_FAILED,
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

        updateNetwork(response?.stateMessage?.response?.message, idWorkout,idExercise)

    }

    private suspend fun updateNetwork(message : String?, idWorkout: String, idExercise: String){
        if(message.equals(REMOVE_WORKOUT_EXERCISE_SUCCESS)) {
            safeApiCall(IO){
                exerciseNetworkDataSource.removeExerciseFromWorkout(idWorkout,idExercise)
            }
        }
    }

    companion object{
        val REMOVE_WORKOUT_EXERCISE_SUCCESS  = "Successfully deleted added exercise to workout."
        val REMOVE_WORKOUT_EXERCISE_FAILED  = "Failed deleting exercise from workout."
    }
}