package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutTypes(
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource
) {

    fun getWorkoutTypes(
        query : String,
        filterAndOrder : String,
        page : Int,
        stateEvent : StateEvent
    ): Flow<DataState<HomeViewState>?> = flow {

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            workoutTypeCacheDataSource.getWorkoutTypes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<HomeViewState, List<WorkoutType>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<HomeViewState>? {

                var message : String? = GET_WORKOUTTYPES_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_WORKOUTTYPES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HomeViewState(listWorkoutTypes = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_WORKOUTTYPES_SUCCESS = "Successfully retrieved list of workoutTypes."
        val GET_WORKOUTTYPES_NO_MATCHING_RESULTS = "There are no workoutTypes that match that query."
        val GET_WORKOUTTYPES_FAILED = "Failed to retrieve the list of workoutTypes."
    }
}