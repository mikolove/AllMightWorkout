package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RemoveWorkout<ViewState>(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource
) {

    fun removeWorkout(
        workout : Workout,
        stateEvent : StateEvent
    ) : Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.removeWorkout(workout.idWorkout)
        }

        val response = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {
                return if( resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = DELETE_WORKOUT_SUCCESS,
                            uiComponentType = UIComponentType.SimpleSnackBar(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent)

                }else{
                    DataState.data(
                        response = Response(
                            message = DELETE_WORKOUT_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)
                }
            }
        }.getResult()

        emit(response)

        //Update network
        updateNetwork(response?.stateMessage?.response?.message, workout)

    }

    private suspend fun updateNetwork(cacheResponse : String?, deletedWorkout : Workout) {
        if(cacheResponse.equals(DELETE_WORKOUT_SUCCESS)){
            safeApiCall(IO){
                workoutNetworkDataSource.removeWorkout(deletedWorkout.idWorkout)
            }

            safeApiCall(IO){
                workoutNetworkDataSource.insertDeleteWorkout(deletedWorkout)
            }
        }
    }

    companion object{
        val DELETE_WORKOUT_SUCCESS = "Successfully deleted workout"
        val DELETE_WORKOUT_FAILED = "Failed deleting workout"
        val DELETE_WORKOUT_ARE_YOU_SURE = "Are you sure to delete this workout ?"
    }
}