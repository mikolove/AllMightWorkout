package com.mikolove.core.interactors.exercise


import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.cache.CacheResponseHandler
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.state.GenericMessageInfo
import com.mikolove.core.domain.state.MessageType
import com.mikolove.core.domain.state.UIComponentType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertExerciseSet(
    private val exerciseSetFactory : ExerciseSetFactory
) {

   /* fun execute(
        idExerciseSet : String? = null,
        reps : Int,
        weight : Int,
        time : Int,
        restTime : Int,
        order : Int,
        idExercise : String,
    ) : Flow<DataState<Long>?> = flow {

        emit(DataState.loading())

        val newExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = idExerciseSet ?: UUID.randomUUID().toString(),
            reps = reps,
            weight = weight,
            time = time,
            restTime = restTime,
            order = order,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            exerciseSetCacheDataSource.insertExerciseSet(exerciseSet = newExerciseSet, idExercise = idExercise)
        }

        val cacheResponse = object : CacheResponseHandler<Long, Long>(
            response = cacheResult,
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<Long> {
                return if(resultObj>0){

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("InsertExerciseSet.Success")
                            .title("")
                            .description(INSERT_EXERCISE_SET_SUCCESS)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = resultObj)

                }else{

                    DataState.data(
                        message = GenericMessageInfo.Builder()
                            .id("InsertExerciseSet.Error")
                            .title("")
                            .description(INSERT_EXERCISE_SET_FAILED)
                            .uiComponentType(UIComponentType.None)
                            .messageType(MessageType.Success),
                        data = null)

                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse.message?.description, newExerciseSet,idExercise)
   }

    private suspend fun updateNetwork(cacheResponse : String?, newExerciseSet : ExerciseSet, idExercise: String){
        if(cacheResponse.equals(INSERT_EXERCISE_SET_SUCCESS)){

            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertExerciseSet(newExerciseSet,idExercise = idExercise)
            }

        }
    }

    companion object{
        val INSERT_EXERCISE_SET_SUCCESS = "Successfully inserted new exercise set."
        val INSERT_EXERCISE_SET_FAILED  = "Failed inserting new exercise set."
    }*/
}