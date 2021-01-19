package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class
InsertWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val workoutFactory: WorkoutFactory
) {

    fun insertWorkout(
        idWorkout : String? = null,
        name : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ManageWorkoutViewState>?> = flow{

        val newWorkout = workoutFactory.createWorkout(
            idWorkout = idWorkout?: UUID.randomUUID().toString(),
            name = name,
            exercises = null,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.insertWorkout(newWorkout)
        }

        val cacheResponse = object : CacheResponseHandler<ManageWorkoutViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ManageWorkoutViewState>? {
                return if (resultObj > 0) {

                    val viewState = ManageWorkoutViewState(newWorkout = newWorkout)
                    DataState.data(
                        response = Response(
                            message = INSERT_WORKOUT_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent)

                } else {

                    DataState.data(
                        response = Response(
                            message = INSERT_WORKOUT_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent)
                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse?.stateMessage?.response?.message, newWorkout)
    }


    private suspend fun updateNetwork(cacheResponse : String?, newWorkout : Workout) {
        if(cacheResponse.equals(INSERT_WORKOUT_SUCCESS)){

            safeApiCall(IO){
                workoutNetworkDataSource.insertWorkout(newWorkout)
            }
        }
    }

    companion object{
        val INSERT_WORKOUT_SUCCESS = "Successfully inserted new workout."
        val INSERT_WORKOUT_FAILED  = "Failed inserting new workout."
    }
}