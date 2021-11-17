package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddExerciseToWorkout(
    private val workoutCacheDataSource : WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val exerciseCacheDataSource: ExerciseCacheDataSource,
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource,
    private val dateUtil: DateUtil
) {

  /*  fun addExerciseToWorkout(
        idWorkout : String,
        idExercise : String,
        stateEvent : StateEvent
    ) : Flow<DataState<WorkoutViewState>?> = flow {

        //Update exerciseIds
        val updatedExerciseIdsDate = dateUtil.getCurrentTimestamp()
        val isExerciseIdsUpdated = updateExerciseIdsUpdatedAt(idWorkout, updatedExerciseIdsDate )

        //If it fails stop the process
        if(isExerciseIdsUpdated == 0){

            val response = DataState.data(
                response = Response(
                    message = RemoveExerciseFromWorkout.REMOVE_WORKOUT_EXERCISE_UPDATE_FAILED,
                    uiComponentType = UIComponentType.Toast(),
                    messageType = MessageType.Error()
                ),
                data = null,
                stateEvent = stateEvent
            ) as DataState<WorkoutViewState>?

            emit(response)

        //If not continue
        }else{

            val cacheResult = safeCacheCall(IO){
                exerciseCacheDataSource.addExerciseToWorkout(idWorkout,idExercise)
            }

            val response = object : CacheResponseHandler<WorkoutViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<WorkoutViewState>? {

                    return if(resultObj>0){

                        val viewState = WorkoutViewState(lastWorkoutExerciseState = true)
                        DataState.data(
                            response = Response(
                                message = INSERT_WORKOUT_EXERCISE_SUCCESS,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Success()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )

                    }else{

                        val viewState = WorkoutViewState(lastWorkoutExerciseState = false)
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

            updateNetwork(response?.message?.response?.message,idWorkout,idExercise,updatedExerciseIdsDate)
        }

    }

    private suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, dateUpdated: String) : Int{

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout, dateUpdated)
        }

        val cacheResponse = object : CacheResponseHandler<Int,Int>(
            response = cacheResult,
            stateEvent = null
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int>? {
                return DataState.data(
                    response = null,
                    data = resultObj,
                    stateEvent = null
                )
            }
        }.getResult()

        return cacheResponse?.data ?: 0
    }

    private suspend fun updateNetwork(message : String? , idWorkout: String, idExercise: String, dateUpdated : String){
        if(message.equals(INSERT_WORKOUT_EXERCISE_SUCCESS)){
            safeApiCall(IO){
                exerciseNetworkDataSource.addExerciseToWorkout(idWorkout,idExercise)
            }
            safeApiCall(IO){
                workoutNetworkDataSource.updateExerciseIdsUpdatedAt(idWorkout,dateUpdated)
            }
        }
    }
*/
    companion object{
        val INSERT_WORKOUT_EXERCISE_SUCCESS  = "Successfully inserted added exercise to workout."
        val INSERT_WORKOUT_EXERCISE_UPDATE_FAILED  = "Failed to update last insert exerciseIds."
        val INSERT_WORKOUT_EXERCISE_FAILED  = "Failed adding exercise to workout."
    }

}