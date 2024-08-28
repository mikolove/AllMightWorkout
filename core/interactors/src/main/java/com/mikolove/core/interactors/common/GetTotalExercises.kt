package com.mikolove.core.interactors.common

import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource

class GetTotalExercises(
    val exerciseCacheDataSource: ExerciseCacheDataSource
){

   /* inline fun<reified ViewState> getTotalExercises(
        stateEvent: StateEvent
    ) : Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.getTotalExercises()
        }

        val response = object : CacheResponseHandler<ViewState,Int>(
            response =cacheResult,
            stateEvent = GetTotalExercisesEvent()
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {


                val viewState = when(ViewState::class){
                    ExerciseViewState::class -> ExerciseViewState(
                        totalExercises = resultObj
                    )
                    WorkoutViewState::class -> WorkoutViewState(
                        totalExercises = resultObj
                    )
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_EXERCISES_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState as ViewState,
                    stateEvent = stateEvent
                )
            }

        }.getResult()

        emit(response)
    }*/

    companion object{
        val GET_TOTAL_EXERCISES_SUCCESS = "Successfully retrieved the number of exercises from the cache."
        val GET_TOTAL_EXERCISES_FAILED = "Failed to retrieved the number of exercises from the cache."
    }
}