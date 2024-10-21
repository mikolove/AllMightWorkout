package com.mikolove.core.interactors.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetNetworkDataSource

class RemoveExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

  /*  fun removeExerciseSet(
        exerciseSet : ExerciseSet,
        idExercise : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {


        val cacheResult = safeCacheCall(IO){
            exerciseSetCacheDataSource.removeExerciseSetById(exerciseSet.idExerciseSet,idExercise)
        }

        val response = object : CacheResponseHandler<ExerciseViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                return if(resultObj>0){
                    DataState.data(
                        response = Response(
                            message = DELETE_EXERCISE_SET_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = DELETE_EXERCISE_SET_FAILED,
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

        updateNetwork(response?.message?.response?.message,exerciseSet,idExercise)
    }

    private suspend fun updateNetwork(response : String? , exerciseSet: ExerciseSet, idExercise: String){
        if(response.equals(DELETE_EXERCISE_SET_SUCCESS)){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.removeExerciseSetById(exerciseSet.idExerciseSet,idExercise)
            }
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertDeletedExerciseSet(exerciseSet)
            }
        }
    }*/
    companion object{
        val DELETE_EXERCISE_SET_SUCCESS = "Successfully deleted exercise set."
        val DELETE_EXERCISE_SET_FAILED = "Failed deleting exercise set."
    }

}