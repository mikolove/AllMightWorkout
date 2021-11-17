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
   /* private var onAddError: Boolean = false

    fun insertMultipleExerciseSet(
        sets : List<ExerciseSet>,
        idExercise : String,
        stateEvent: StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {


        val successfulAdd : ArrayList<ExerciseSet> = ArrayList()

        sets.forEach{ set ->

            val cacheResult = safeCacheCall(IO){
                exerciseSetCacheDataSource.insertExerciseSet(set,idExercise)
            }

            val response = object : CacheResponseHandler<ExerciseViewState,Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Long): DataState<ExerciseViewState>? {
                    //-1 if insert error occur
                    if(resultObj < 0){
                        onAddError = true
                    }else{
                        successfulAdd.add(set)
                    }
                    return null
                }
            }.getResult()

            if (response?.message?.response?.message?.contains(stateEvent.errorInfo()) == true){
                onAddError = true
            }
        }

        if(onAddError){
            emit(
                DataState.data<ExerciseViewState>(
                    response = Response(
                        message = ADD_EXERCISE_SETS_ERRORS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Error()
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )

        }else{
            emit(
                DataState.data<ExerciseViewState>(
                    response = Response(
                        message = ADD_EXERCISE_SETS_SUCCESS,
                        uiComponentType = UIComponentType.Toast(),
                        messageType = MessageType.Success()
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

        //updateNetwork(successfulAdd,idExercise)

    }

    private suspend fun updateNetwork(successfulAdd : ArrayList<ExerciseSet>,idExercise : String){
        successfulAdd.forEach{ set ->
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertExerciseSet(exerciseSet = set,idExercise = idExercise)
            }
        }
    }*/

    companion object{
        val ADD_EXERCISE_SETS_SUCCESS = "Successfully added exercise sets."
        val ADD_EXERCISE_SETS_ERRORS = "Not all the exercise set were added. Errors occurs."
    }
}