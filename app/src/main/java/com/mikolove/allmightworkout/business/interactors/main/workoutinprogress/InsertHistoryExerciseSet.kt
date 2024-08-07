package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory

class InsertHistoryExerciseSet(
    private val historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
    private val historyExerciseSetFactory: HistoryExerciseSetFactory
) {

  /*  fun insertHistoryExerciseSet(
        idHistoryExerciseSet: String?,
        reps: Int,
        weight: Int,
        time: Int,
        restTime: Int,
        started_at: String,
        ended_at: String,
        historyExerciseId: String,
        stateEvent: StateEvent
    ): Flow<DataState<WorkoutInProgressViewState>?> = flow {

        //Create history exerciseSet
        val historyExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
            idHistoryExerciseSet = idHistoryExerciseSet ?: UUID.randomUUID().toString(),
            reps =reps,
            weight= weight,
            time = time,
            restTime = restTime,
            started_at = started_at,
            ended_at = ended_at,
            created_at = null)

        val cacheResult = safeCacheCall(Dispatchers.IO){
            historyExerciseSetCacheDataSource.insertHistoryExerciseSet(historyExerciseSet,historyExerciseId)
        }

        val response = object : CacheResponseHandler<WorkoutInProgressViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<WorkoutInProgressViewState>? {
                return if(resultObj >0){
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_EXERCISE_SET_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = WorkoutInProgressViewState(lastHistoryExerciseSetInserted = historyExerciseSet),
                        stateEvent = stateEvent
                    )
                }else{
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_EXERCISE_SET_FAILED,
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
        const val INSERT_HISTORY_EXERCISE_SET_SUCCESS = "Successfully inserted history exercise set."
        const val INSERT_HISTORY_EXERCISE_SET_FAILED  = "Failed inserting history exercise set."
    }

}