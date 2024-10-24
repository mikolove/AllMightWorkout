package com.mikolove.core.interactors.exercise

import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource


class RemoveMultipleExerciseSet(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
    private val exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
) {

   /* private var onDeleteError : Boolean = false

    fun removeMultipleExerciseSet(
        sets : List<ExerciseSet>,
        idExercise : String,
        stateEvent: StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow {

        val successfulDeletes : ArrayList<ExerciseSet> = ArrayList()

        for(set in sets){

            val cacheResult = safeCacheCall(IO){
                exerciseSetCacheDataSource.removeExerciseSetById(set.idExerciseSet,idExercise)
            }

            val response = object : CacheResponseHandler<ExerciseViewState,Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Int): DataState<ExerciseViewState>? {
                    if(resultObj <0){
                        onDeleteError = true
                    }else {
                        successfulDeletes.add(set)
                    }
                    return null
                }
            }.getResult()

            if(response?.message?.response?.message?.contains(stateEvent.errorInfo()) == true){
                onDeleteError = true
            }
        }

        if(onDeleteError){
            emit(DataState.data<ExerciseViewState>(
                    response = Response(
                        message = DELETE_EXERCISE_SETS_ERRORS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error(),
                    ),
                    data = null,
                    stateEvent = stateEvent
                ))
        }else{
            emit(DataState.data<ExerciseViewState>(
                response = Response(
                    message = DELETE_EXERCISE_SETS_SUCCESS,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Error(),
                ),
                data = null,
                stateEvent = stateEvent
            ))
        }

        //updateNetwork(successfulDeletes,idExercise)
    }


    private suspend fun updateNetwork(successfulDeletes: ArrayList<ExerciseSet>,idExercise: String){

        val ids = successfulDeletes.mapIndexed { index, set -> set.idExerciseSet }

        safeApiCall(IO){
            exerciseSetNetworkDataSource.removeExerciseSetsById(ids,idExercise)
        }

        for (set in successfulDeletes){
            safeApiCall(IO){
                exerciseSetNetworkDataSource.insertDeletedExerciseSet(set)
            }
        }
    }*/

    companion object{
        val DELETE_EXERCISE_SETS_SUCCESS = "Successfully deleted exercise sets."
        val DELETE_EXERCISE_SETS_ERRORS = "Not all the exercise sets were deleted. Errors occurs."
    }
}