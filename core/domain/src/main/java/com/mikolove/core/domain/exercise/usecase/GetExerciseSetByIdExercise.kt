package com.mikolove.core.domain.exercise.usecase

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource

class GetExerciseSetByIdExercise(
    private val exerciseSetCacheDataSource: ExerciseSetCacheDataSource
)  {

   /* fun getExerciseSetByIdExercise(
        idExercise : String,
        stateEvent : StateEvent
    ) : Flow<DataState<ExerciseViewState>?> = flow{

        val cacheResult = safeCacheCall(IO){
            exerciseSetCacheDataSource.getExerciseSetByIdExercise(idExercise)
        }

        val response = object : CacheResponseHandler<ExerciseViewState,List<ExerciseSet>>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<ExerciseSet>): DataState<ExerciseViewState>? {

                val sets = if(resultObj.size>0) ArrayList(resultObj) else ArrayList()

                val message = if(resultObj.size>0)
                    GET_EXERCISE_SET_BY_ID_EXERCISE_SUCCESS
                else
                    GET_EXERCISE_SET_BY_ID_EXERCISE_NO_RESULT

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = ExerciseViewState(cachedExerciseSetsByIdExercise = sets),
                    stateEvent = stateEvent
                )

            }
        }.getResult()

        emit(response)
    }*/

    companion object{
        val GET_EXERCISE_SET_BY_ID_EXERCISE_SUCCESS = "Successfully retrieved list of exercise sets."
        val GET_EXERCISE_SET_BY_ID_EXERCISE_NO_RESULT = "Successfully retrieved the bodyparts from the cache."
        val GET_EXERCISE_SET_BY_ID_EXERCISE__FAILED = "Failed to retrieve the list of exercise sets."
    }
}