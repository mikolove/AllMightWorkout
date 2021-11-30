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

    fun execute(
        idWorkout : String,
        idExercise : String
    ) : Flow<DataState<Long>?> = flow {

        //Update exerciseIds
        val updatedExerciseIdsDate = dateUtil.getCurrentTimestamp()
        val isExerciseIdsUpdated = updateExerciseIdsUpdatedAt(idWorkout, updatedExerciseIdsDate )

        //If it fails stop the process
        if(isExerciseIdsUpdated == 0){

            emit(DataState<Long>(
                message = GenericMessageInfo.Builder()
                    .id("AddExerciseToWorkout.UpdateFail")
                    .title("AddExerciseToWorkout update fail")
                    .description(INSERT_WORKOUT_EXERCISE_UPDATE_FAILED)
                    .uiComponentType(UIComponentType.Toast)
                    .messageType(MessageType.Error)
            ))

        //If not continue
        }else{

            val cacheResult = safeCacheCall(IO){
                exerciseCacheDataSource.addExerciseToWorkout(idWorkout,idExercise)
            }

            val response = object : CacheResponseHandler<Long, Long>(
                response = cacheResult
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<Long>? {

                    return if(resultObj>0){
                        DataState<Long>(
                            message = GenericMessageInfo.Builder()
                                .id("AddExerciseToWorkout.Success")
                                .title("AddExerciseToWorkout success")
                                .description(INSERT_WORKOUT_EXERCISE_SUCCESS)
                                .uiComponentType(UIComponentType.None)
                                .messageType(MessageType.Success),
                            data = resultObj
                        )

                    }else{
                        DataState<Long>(
                            message = GenericMessageInfo.Builder()
                                .id("AddExerciseToWorkout.Failed")
                                .title("AddExerciseToWorkout failed")
                                .description(INSERT_WORKOUT_EXERCISE_FAILED)
                                .uiComponentType(UIComponentType.Toast)
                                .messageType(MessageType.Error)
                        )
                    }
                }
            }.getResult()

            emit(response)

            updateNetwork(response?.message?.description,idWorkout,idExercise,updatedExerciseIdsDate)
        }

    }

    private suspend fun updateExerciseIdsUpdatedAt(idWorkout: String, dateUpdated: String) : Int{

        val cacheResult = safeCacheCall(IO){
            workoutCacheDataSource.updateExerciseIdsUpdatedAt(idWorkout, dateUpdated)
        }

        val cacheResponse = object : CacheResponseHandler<Int,Int>(
            response = cacheResult
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<Int> {
                return DataState.data(
                    message = null,
                    data = resultObj
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

    companion object{
        val INSERT_WORKOUT_EXERCISE_SUCCESS  = "Successfully inserted added exercise to workout."
        val INSERT_WORKOUT_EXERCISE_UPDATE_FAILED  = "Failed to update last insert exerciseIds."
        val INSERT_WORKOUT_EXERCISE_FAILED  = "Failed adding exercise to workout."
    }

}