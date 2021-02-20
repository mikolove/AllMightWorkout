package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeStateEvent.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource
){

    fun getTotalExercises(
        stateEvent: StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.getTotalExercises()
        }

        val response = object : CacheResponseHandler<ExerciseViewState,Int>(
            response =cacheResult,
            stateEvent = GetTotalExercisesEvent()
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                val viewState = ExerciseViewState(
                    totalExercises = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_EXERCISES_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }

        }.getResult()

        emit(response)
    }

    companion object{
        val GET_TOTAL_EXERCISES_SUCCESS = "Successfully retrieved the number of exercises from the cache."
        val GET_TOTAL_EXERCISES_FAILED = "Failed to retrieved the number of exercises from the cache."
    }
}