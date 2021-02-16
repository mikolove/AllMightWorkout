package com.mikolove.allmightworkout.business.interactors.main.home

/*
TODO: Define if usefull thinking not

class GetExercisesFromWorkoutId(
    private val exerciseCacheDataSource: ExerciseCacheDataSource
) {

    fun getExercisesFromWorkoutId(
      idWorkout : String,
      stateEvent : StateEvent
    ): Flow<DataState<HomeViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            exerciseCacheDataSource.getExercisesByWorkout(idWorkout = idWorkout)
        }

        val response = object : CacheResponseHandler<HomeViewState, List<Exercise>?>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<Exercise>?): DataState<HomeViewState>? {

                var message : String? = GET_EXERCISES_FROM_WORKOUT_SUCCESS
                var uiComponentType : UIComponentType = UIComponentType.None()

                if(resultObj?.size == 0){
                    message = GET_EXERCISES_FROM_WORKOUT_NO_RESULT
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType =  MessageType.Success()
                    ),
                    data = HomeViewState(listExercisesFromWorkoutId = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )

             }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_EXERCISES_FROM_WORKOUT_SUCCESS   = "Successfully retrieved list of exercises for specified workout Id."
        val GET_EXERCISES_FROM_WORKOUT_NO_RESULT = "No list of exercises retrieved for specified workout Id."
        val GET_EXERCISES_FROM_WORKOUT_FAILED    = "Failed to retrieve the list of exercises for specified workout Id."
    }
}*/
