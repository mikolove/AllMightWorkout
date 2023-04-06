package com.mikolove.allmightworkout.business.interactors.main.exercise


import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertExerciseSet(
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
    private val exerciseSetFactory : ExerciseSetFactory
) {

    fun execute(
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

        val cacheResponse = object :CacheResponseHandler<Long, Long>(
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

        updateNetwork(cacheResponse?.message?.description, newExerciseSet,idExercise)
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
    }
}