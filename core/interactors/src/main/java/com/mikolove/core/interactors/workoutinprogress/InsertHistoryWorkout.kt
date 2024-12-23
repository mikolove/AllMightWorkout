package com.mikolove.core.interactors.workoutinprogress

import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource

class InsertHistoryWorkout(
    private val analyticsCacheDataSource: AnalyticsCacheDataSource,
    private val historyWorkoutFactory : HistoryWorkoutFactory
) {

   /* fun insertHistoryWorkout(
        idHistoryWorkout : String?,
        name : String,
        started_at : String,
        ended_at : String,
        stateEvent : StateEvent
    ) : Flow<DataState<WorkoutInProgressViewState>?> = flow {

        //Create history workout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = idHistoryWorkout ?: UUID.randomUUID().toString(),
            name = name,
            historyExercises = null,
            started_at = started_at,
            ended_at = ended_at,
            created_at = null)

        val cacheResult = safeCacheCall(IO){
            historyWorkoutCacheDataSource.insertHistoryWorkout(historyWorkout)
        }

        val response = object : CacheResponseHandler<WorkoutInProgressViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<WorkoutInProgressViewState>? {
                return if(resultObj >0){
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_WORKOUT_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = WorkoutInProgressViewState(lastHistoryWorkoutInserted = historyWorkout),
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_WORKOUT_FAILED,
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
        const val INSERT_HISTORY_WORKOUT_SUCCESS = "Successfully inserted history workout."
        const val INSERT_HISTORY_WORKOUT_FAILED  = "Failed inserting history workout."
    }
}