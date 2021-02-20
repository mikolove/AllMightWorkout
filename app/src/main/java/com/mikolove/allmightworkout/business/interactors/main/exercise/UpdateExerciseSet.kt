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


class UpdateExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {


    fun updateExerciseSet(
        exerciseSet : ExerciseSet,
        idExercise : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseSetCacheDataSource.updateExerciseSet(
                exerciseSet.idExerciseSet,
                exerciseSet.reps,
                exerciseSet.weight,
                exerciseSet.time,
                exerciseSet.restTime,
                exerciseSet.updatedAt,
                idExercise
            )
        }

        val response = object : CacheResponseHandler<ExerciseViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                return if(resultObj>0){

                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_SET_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )

                }else{

                    DataState.data(
                        response = Response(
                            message = UPDATE_EXERCISE_SET_FAILED,
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

    private suspend fun updateNetwork(response: String?, exerciseSet: ExerciseSet, idExercise: String){
        if(response.equals(UPDATE_EXERCISE_SET_SUCCESS)){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.updateExerciseSet(exerciseSet, idExercise)
            }
        }
    }
    companion object{
        val UPDATE_EXERCISE_SET_SUCCESS = "Successfully updated exercise set."
        val UPDATE_EXERCISE_SET_FAILED  = "Failed updated exercise set."
    }

}