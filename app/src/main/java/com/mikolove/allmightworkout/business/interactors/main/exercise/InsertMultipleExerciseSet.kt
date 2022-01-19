package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.util.safeApiCall
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertMultipleExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

    // set true if an error occurs when adding any of the sets
    private var onAddError: Boolean = false

    fun execute(
        sets : List<ExerciseSet>,
        idExercise : String,
    ) : Flow<DataState<List<Long>>?> = flow {

        emit(DataState.loading<List<Long>>())

        val successfullAdd = mutableListOf<ExerciseSet>()
        val successfullInsert = mutableListOf<Long>()

        sets.forEach{ set ->

            val cacheResult = safeCacheCall(IO){
                exerciseSetCacheDataSource.insertExerciseSet(set,idExercise)
            }

            val response = object : CacheResponseHandler<Long,Long>(
                response = cacheResult,
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<Long>? {
                    //-1 if insert error occur
                    if(resultObj < 0){
                        onAddError = true
                    }else{
                        successfullAdd.add(set)
                        successfullInsert.add(resultObj)
                    }
                    return null
                }
            }.getResult()

            if (response?.message?.messageType is MessageType.Error){
                onAddError = true
            }
        }

        if(onAddError){
            emit(
                DataState.error<List<Long>>(
                    message = GenericMessageInfo.Builder()
                        .id("InsertMultipleExerciseSet.Error")
                        .title("")
                        .description(ADD_EXERCISE_SETS_ERRORS)
                        .uiComponentType(UIComponentType.Toast)
                        .messageType(MessageType.Error)
                )
            )

        }else{
            emit(
                DataState.data(
                    message = GenericMessageInfo.Builder()
                        .id("InsertMultipleExerciseSet.Success")
                        .title("")
                        .description(ADD_EXERCISE_SETS_SUCCESS)
                        .uiComponentType(UIComponentType.None)
                        .messageType(MessageType.Success),
                    data = successfullInsert.toList()
                )
            )
        }

        updateNetwork(successfullAdd,idExercise)

    }

    private suspend fun updateNetwork(successfulAdd : List<ExerciseSet>,idExercise : String){
        successfulAdd.forEach{ set ->
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertExerciseSet(exerciseSet = set,idExercise = idExercise)
            }
        }
    }

    companion object{
        val ADD_EXERCISE_SETS_SUCCESS = "Successfully added exercise sets."
        val ADD_EXERCISE_SETS_ERRORS = "Not all the exercise set were added. Errors occurs."
    }
}