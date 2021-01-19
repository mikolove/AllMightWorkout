package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheResponseHandler
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.util.safeCacheCall
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseFactory
import com.mikolove.allmightworkout.business.domain.state.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertHistoryExercise(
    private val historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
    private val historyExerciseFactory: HistoryExerciseFactory
) {

    fun insertHistoryExercise(
        idHistoryExercise : String?,
        name : String,
        bodyPart : String,
        workoutType : String,
        exerciseType : String,
        started_at : String,
        ended_at : String,
        stateEvent : StateEvent
    ): Flow<DataState<WorkoutViewState>?> = flow {

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
            historyExerciseCacheDataSource.insertHistoryExercise(historyExercise)
        }

        val response = object : CacheResponseHandler<WorkoutViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<WorkoutViewState>? {
                return if(resultObj >0){
                    DataState.data(
                        response = Response(
                            message = INSERT_HISTORY_EXERCISE_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = WorkoutViewState(lastHistoryExerciseInserted = historyExercise),
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

    companion object{
        const val INSERT_HISTORY_EXERCISE_SUCCESS = "Successfully inserted history exercise."
        const val INSERT_HISTORY_EXERCISE_FAILED  = "Failed inserting history exercise."
    }
}