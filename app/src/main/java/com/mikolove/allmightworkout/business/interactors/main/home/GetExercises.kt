package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetExercises(
    private val exerciseCacheDataSource: ExerciseCacheDataSource
) {

    fun getExercises(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent : StateEvent
    ) : Flow<DataState<HomeViewState>?> = flow{

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            exerciseCacheDataSource.getExercises(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<HomeViewState, List<Exercise>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>): DataState<HomeViewState>? {

                var message : String? = GET_EXERCISES_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_EXERCISES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HomeViewState(listExercises = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)

    }

    companion object{
        val GET_EXERCISES_SUCCESS = "Successfully retrieved list of exercises."
        val GET_EXERCISES_NO_MATCHING_RESULTS = "There are no exercises that match that query."
        val GET_EXERCISES_FAILED = "Failed to retrieve the list of exercises."
    }
}