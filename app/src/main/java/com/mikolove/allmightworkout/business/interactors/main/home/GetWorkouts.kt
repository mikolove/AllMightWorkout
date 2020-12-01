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

class GetWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource
) {

    fun getWorkouts(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent : StateEvent
    ) : Flow<DataState<HomeViewState>?>  = flow{

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkouts(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<HomeViewState,List<Workout>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<Workout>): DataState<HomeViewState>? {

                var message : String? = GET_WORKOUTS_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_WORKOUTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HomeViewState(listWorkouts = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)

    }

    companion object{
        val GET_WORKOUTS_SUCCESS = "Successfully retrieved list of workouts."
        val GET_WORKOUTS_NO_MATCHING_RESULTS = "There are no workouts that match that query."
        val GET_WORKOUTS_FAILED = "Failed to retrieve the list of workouts."
    }
}
