package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveMultipleExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    // set true if an error occurs when deleting any of the exercises from cache
    private var onDeleteError: Boolean = false

    fun removeMultipleExercises(
        exercises : List<Exercise>,
        stateEvent : StateEvent
    ): Flow<DataState<HomeViewState>?> = flow {

        val successfulDeletes : ArrayList<Exercise> = ArrayList()

        for(exercise in exercises){

            val cacheResult = safeCacheCall(IO) {
                exerciseCacheDataSource.removeExerciseById(exercise.idExercise)
            }

            val response = object : CacheResponseHandler<HomeViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<HomeViewState>? {
                    if (resultObj < 0) { // error
                        onDeleteError = true
                    } else { // success
                        successfulDeletes.add(exercise)
                    }
                    return null
                }
            }.getResult()

            //Check for random errors
            if (response?.stateMessage?.response?.message?.contains(stateEvent.errorInfo()) == true) {
                onDeleteError = true
            }

        }

        if(onDeleteError){
            emit(
                DataState.data<HomeViewState>(
                    response = Response(
                        message = DELETE_EXERCISES_ERRORS,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()),
                    data = null,
                    stateEvent = stateEvent
                ))
        }else{
            emit(
                DataState.data<HomeViewState>(
                    response = Response(
                        message = DELETE_EXERCISES_SUCCESS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()),
                    data = null,
                    stateEvent = stateEvent
                ))
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
    }

}