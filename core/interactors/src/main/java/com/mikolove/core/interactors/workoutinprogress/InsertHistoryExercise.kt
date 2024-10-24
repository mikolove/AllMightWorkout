package com.mikolove.core.interactors.workoutinprogress

import com.mikolove.core.domain.analytics.HistoryExerciseFactory

class InsertHistoryExercise(
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseFactory: HistoryExerciseFactory
) {

   /* fun insertHistoryExercise(
        idHistoryExercise : String?,
        name : String,
        bodyPart : String,
        workoutType : String,
        exerciseType : String,
        started_at : String,
        ended_at : String,
        idHistoryWorkout : String,
        stateEvent : StateEvent
    ): Flow<DataState<WorkoutInProgressViewState>?> = flow {

        //Create history exercise
        val historyExercise = historyExerciseFactory.createHistoryExercise(
            idHistoryExercise = idHistoryExercise ?: UUID.randomUUID().toString(),
            name = name,
            bodyPart = bodyPart,
            workoutType = workoutType,
            exerciseType = exerciseType,
            historySets = null,
            started_at = started_at,
            ended_at = ended_at,
            created_at = null
        )

        val cacheResult = safeCacheCall(IO){
            historyExerciseCacheDataSource.insertHistoryExercise(historyExercise,idHistoryWorkout)
        }

        val response = object : CacheResponseHandler<WorkoutInProgressViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<WorkoutInProgressViewState>? {
                return if(resultObj >0){
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = WorkoutInProgressViewState(lastHistoryExerciseInserted = historyExercise),
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_EXERCISE_FAILED,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)
    }
*/
    companion object{
        const val INSERT_HISTORY_EXERCISE_SUCCESS = "Successfully inserted history exercise."
        const val INSERT_HISTORY_EXERCISE_FAILED  = "Failed inserting history exercise."
    }
}