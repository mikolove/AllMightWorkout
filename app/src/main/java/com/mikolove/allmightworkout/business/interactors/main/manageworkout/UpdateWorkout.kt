package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class UpdateWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
) {

    fun updateWorkout(
        workout : Workout,
        stateEvent : StateEvent
    ): Flow<DataState<ManageWorkoutViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.updateWorkout(
                workout.idWorkout,
                workout.name,
                workout.isActive,
                workout.updatedAt
            )
        }

        val response = object : CacheResponseHandler<ManageWorkoutViewState,Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ManageWorkoutViewState>? {
                return if(resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = UPDATE_WORKOUT_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
                else {
                    DataState.data(
                        response = Response(
                            message = UPDATE_WORKOUT_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }

            }
        }.getResult()

        emit(response)

        updateNetwork(response?.stateMessage?.response?.message, workout)

    }

    private suspend fun updateNetwork(response: String?, workout: Workout){
        if(response.equals(UPDATE_WORKOUT_SUCCESS)){
            safeApiCall(IO){
                workoutNetworkDataSource.updateWorkout(workout.idWorkout,workout)
            }
        }

    }

    companion object{
        val UPDATE_WORKOUT_SUCCESS = "Successfully updated workout."
        val UPDATE_WORKOUT_FAILED  = "Failed updated workout."
    }
}