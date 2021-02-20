package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

    fun removeExerciseSet(
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

        updateNetwork(response?.stateMessage?.response?.message,exerciseSet,idExercise)
    }

    private suspend fun updateNetwork(response : String? , exerciseSet: ExerciseSet, idExercise: String){
        if(response.equals(DELETE_EXERCISE_SET_SUCCESS)){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.removeExerciseSetById(exerciseSet.idExerciseSet,idExercise)
            }
            //TODO : Need to delete this maybe

            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertDeletedExerciseSet(exerciseSet)
            }
        }
    }
    companion object{
        val DELETE_EXERCISE_SET_SUCCESS = "Successfully deleted exercise set."
        val DELETE_EXERCISE_SET_FAILED = "Failed deleting exercise set."
    }

}