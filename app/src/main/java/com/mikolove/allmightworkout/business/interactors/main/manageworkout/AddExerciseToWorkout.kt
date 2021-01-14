package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddExerciseToWorkout(
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
) {

    fun addExerciseToWorkout(
        idWorkout : String,
        idExercise : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ManageWorkoutViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
           exerciseCacheDataSource.addExerciseToWorkout(idWorkout,idExercise)
        }

        val response = object : CacheResponseHandler<ManageWorkoutViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<ManageWorkoutViewState>? {

                return if(resultObj>0){

                    val viewState = ManageWorkoutViewState(lastWorkoutExerciseState = true)
                    DataState.data(
                        response = Response(
                            message = INSERT_WORKOUT_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )

                }else{

                    val viewState = ManageWorkoutViewState(lastWorkoutExerciseState = false)
                    DataState.data(
                        response = Response(
                            message = INSERT_WORKOUT_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )

                }
            }
        }.getResult()

        emit(response)

        updateNetwork(response?.stateMessage?.response?.message,idWorkout,idExercise)
    }

    private suspend fun updateNetwork(message : String? , idWorkout: String, idExercise: String){
        if(message.equals(INSERT_WORKOUT_EXERCISE_SUCCESS)){
            safeApiCall(IO){
                exerciseNetworkDataSource.addExerciseToWorkout(idWorkout,idExercise)
            }
        }
    }

    companion object{
        val INSERT_WORKOUT_EXERCISE_SUCCESS  = "Successfully inserted added exercise to workout."
        val INSERT_WORKOUT_EXERCISE_FAILED  = "Failed adding exercise to workout."
    }

}