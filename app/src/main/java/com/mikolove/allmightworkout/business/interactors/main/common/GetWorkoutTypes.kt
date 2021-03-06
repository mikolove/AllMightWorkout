package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutTypes(
    val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource
) {

    inline fun <reified ViewState>  getWorkoutTypes(
        query : String,
        filterAndOrder : String,
        page : Int,
        stateEvent : StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        var updatedPage = page
        if(page <= 0) updatedPage = 1

        val cacheResult = safeCacheCall(Dispatchers.IO){
            workoutTypeCacheDataSource.getWorkoutTypes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<ViewState, List<WorkoutType>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<WorkoutType>): DataState<ViewState>? {

                var message : String? = GET_WORKOUTTYPES_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj.size == 0){
                    message = GET_WORKOUTTYPES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }

                val viewState = when(ViewState::class){
                    WorkoutViewState::class -> WorkoutViewState(listWorkoutTypes = ArrayList(resultObj))
                    ExerciseViewState::class -> ExerciseViewState(listWorkoutTypes = ArrayList(resultObj))
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = viewState as ViewState,
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