package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutById(
    private val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun getWorkoutById(
        idWorkout : String,
        stateEvent : StateEvent
    ) : Flow<DataState<HomeViewState>?> =  flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkoutById(idWorkout)
        }

        val response = object : CacheResponseHandler<HomeViewState, Workout?>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Workout?): DataState<HomeViewState>? {

                if(resultObj != null){
                    return DataState.data(
                        response = Response(
                            message = GET_WORKOUT_BY_ID_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                            ),
                        data = HomeViewState(workoutSelected = resultObj),
                        stateEvent = stateEvent)

                }else{

                    return DataState.data(
                        response = Response(
                            message = GET_WORKOUT_BY_ID_FAILED,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)
                }
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_WORKOUT_BY_ID_SUCCESS = "Successfully retrieved workout by id."
        val GET_WORKOUT_BY_ID_FAILED = "No workout found for the specified id."
    }

}