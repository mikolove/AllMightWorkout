package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutById(
    val workoutCacheDataSource: WorkoutCacheDataSource
) {

    inline fun <reified ViewState> execute(
        idWorkout : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> =  flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.getWorkoutById(idWorkout)
        }

        val response = object : CacheResponseHandler<ViewState, Workout>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Workout): DataState<ViewState>? {

                val viewState = when(ViewState::class){
                    WorkoutViewState::class -> WorkoutViewState(workoutSelected = resultObj)
                    WorkoutInProgressViewState:: class -> WorkoutInProgressViewState(workout = resultObj)
                    else -> null
                }

                return DataState.data(
                    response = Response(
                        message = GET_WORKOUT_BY_ID_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                        ),
                    data = viewState as ViewState,
                    stateEvent = stateEvent)
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_WORKOUT_BY_ID_SUCCESS = "Successfully retrieved workout by id."
    }

}