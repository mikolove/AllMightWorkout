package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RemoveMultipleWorkouts(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource
) {

    // set true if an error occurs when deleting any of the workouts from cache
    private var onDeleteError: Boolean = false

    fun removeMultipleWorkouts(
        workouts : List<Workout>,
        stateEvent: StateEvent
    ): Flow<DataState<WorkoutViewState>?> = flow {

        val successfulDeletes : ArrayList<Workout> = ArrayList()

        for(workout in workouts) {

            val cacheResult = safeCacheCall(IO) {
                workoutCacheDataSource.removeWorkout(workout.idWorkout)
            }

            val response = object : CacheResponseHandler<WorkoutViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<WorkoutViewState>? {
                    if (resultObj < 0) { // error
                        onDeleteError = true
                    } else { // success
                        successfulDeletes.add(workout)
                    }
                    return null
                }
            }.getResult()

            //Check for random errors
            if (response?.stateMessage?.response?.message?.contains(stateEvent.errorInfo()) == true) {
                onDeleteError = true
            }

        }

        if(onDeleteError){
            emit(
                DataState.data<WorkoutViewState>(
                        response = Response(
                            message = DELETE_WORKOUTS_ERRORS,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()),
                        data = null,
                        stateEvent = stateEvent
                        ))
        }else{
            emit(
                DataState.data<WorkoutViewState>(
                        response = Response(
                            message = DELETE_WORKOUTS_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()),
                    data = null,
                    stateEvent = stateEvent
                ))
        }

        updateNetwork(successfulDeletes)
    }

    private suspend fun updateNetwork(successfulDeletes: ArrayList<Workout>){
        for (workout in successfulDeletes){
            safeApiCall(IO){
                workoutNetworkDataSource.removeWorkout(workout.idWorkout)
            }

            safeApiCall(IO){
                workoutNetworkDataSource.insertDeleteWorkouts(successfulDeletes)
            }
        }
    }


    companion object{
        val DELETE_WORKOUTS_SUCCESS = "Successfully deleted workouts."
        val DELETE_WORKOUTS_ERRORS = "Not all the workouts were deleted. Errors occurs."
        val DELETE_WORKOUTS_YOU_MUST_SELECT ="You haven't selected any workouts to delete."
        val DELETE_WORKOUTS_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }

}