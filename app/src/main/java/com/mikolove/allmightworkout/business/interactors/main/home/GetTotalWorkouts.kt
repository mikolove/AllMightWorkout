package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun getTotalWorkouts(
        stateEvent: StateEvent
    ) : Flow<DataState<HomeViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getTotalWorkout()
        }

        val response = object : CacheResponseHandler<HomeViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<HomeViewState>? {

                val viewState = HomeViewState(
                    numWorkoutInCache = resultObj
                )

                return DataState.data(
                    response = Response(
                        message = GET_TOTAL_WORKOUT_SUCCESS,
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
        val GET_TOTAL_WORKOUT_SUCCESS = "Successfully retrieved the number of workouts from the cache."
        val GET_TOTAL_WORKOUT_FAILED = "Failed to retrieved the number of workouts from the cache."
    }
}