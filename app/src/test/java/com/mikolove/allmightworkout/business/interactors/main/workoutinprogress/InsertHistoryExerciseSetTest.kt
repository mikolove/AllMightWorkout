package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_HISTORY_EXERCISE_SET_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExerciseSet.Companion.INSERT_HISTORY_EXERCISE_SET_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExerciseSet.Companion.INSERT_HISTORY_EXERCISE_SET_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertHistoryExerciseSet_success_confirmCacheUpdated()
    a) insert a new history exercise set
    b) listen for INSERT_HISTORY_EXERCISE_SET_SUCCESS emission from flow
    c) confirm cache was updated with new history exercise set
2. insertHistoryExerciseSet_failed_confirmCacheUnchanged()
    a) insert a new history exercise set
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_HISTORY_EXERCISE_SET_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError_confirmCacheUnchanged()
    a) insert a new history exercise set
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */

@InternalCoroutinesApi
class InsertHistoryExerciseSetTest {

    //System in test
    private val insertHistoryExerciseSet : InsertHistoryExerciseSet

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val historyExerciseSetCacheDataSource : HistoryExerciseSetCacheDataSource
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private val historyExerciseSetFactory : HistoryExerciseSetFactory

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        historyExerciseSetCacheDataSource = dependencyContainer.historyExerciseSetCacheDataSource
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        historyExerciseSetFactory = dependencyContainer.historyExerciseSetFactory
        insertHistoryExerciseSet =InsertHistoryExerciseSet(
            historyExerciseSetCacheDataSource,
            historyExerciseSetFactory
        )
    }

    @Test
    fun insertHistoryExerciseSet_success_confirmCacheUpdated() = runBlocking {

        //Get exercise set for insertion
        val exerciseSet = exerciseSetCacheDataSource.getExerciseSetById("idExerciseSet1","idExercise1")!!
        exerciseSet.start(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)
        exerciseSet.stop(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)

        //Create history exercise set
        val historyExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
            idHistoryExerciseSet = UUID.randomUUID().toString(),
            reps = exerciseSet.reps,
            weight = exerciseSet.weight,
            time = exerciseSet.time,
            restTime = exerciseSet.time,
            started_at = exerciseSet.startedAt,
            ended_at = exerciseSet.endedAt,
            created_at = null
        )

        //insert it
        insertHistoryExerciseSet.insertHistoryExerciseSet(
            idHistoryExerciseSet = historyExerciseSet.idHistoryExerciseSet,
            reps = historyExerciseSet.reps,
            weight = historyExerciseSet.weight,
            time = historyExerciseSet.time,
            restTime = historyExerciseSet.restTime,
            started_at = historyExerciseSet.startedAt,
            ended_at = historyExerciseSet.endedAt,
            historyExerciseId = "idExercise1",
            stateEvent = InsertHistoryExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?>{
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_HISTORY_EXERCISE_SET_SUCCESS

                )
            }
        })

        //Check cache
        val historyExerciseSetCacheValue = historyExerciseSetCacheDataSource.getHistoryExerciseSetById(historyExerciseSet.idHistoryExerciseSet)
        assertTrue {historyExerciseSetCacheValue == historyExerciseSet}
    }

    @Test
    fun insertHistoryExerciseSet_failed_confirmCacheUnchanged() = runBlocking {

        //Get exercise set for insertion
        val exerciseSet = exerciseSetCacheDataSource.getExerciseSetById("idExerciseSet1","idExercise1")!!


        //Create history exercise set
        val historyExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
            idHistoryExerciseSet = FORCE_GENERAL_FAILURE,
            reps = exerciseSet.reps,
            weight = exerciseSet.weight,
            time = exerciseSet.time,
            restTime = exerciseSet.time,
            started_at = exerciseSet.startedAt,
            ended_at = exerciseSet.endedAt,
            created_at = null
        )

        //insert it
        insertHistoryExerciseSet.insertHistoryExerciseSet(
            idHistoryExerciseSet = historyExerciseSet.idHistoryExerciseSet,
            reps = historyExerciseSet.reps,
            weight = historyExerciseSet.weight,
            time = historyExerciseSet.time,
            restTime = historyExerciseSet.restTime,
            started_at = historyExerciseSet.startedAt,
            ended_at = historyExerciseSet.endedAt,
            stateEvent = InsertHistoryExerciseSetEvent(),
            historyExerciseId = "idExercise1"
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?>{
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_HISTORY_EXERCISE_SET_FAILED

                )
            }
        })

        //Check cache
        val historyExerciseSetCacheValue = historyExerciseSetCacheDataSource.getHistoryExerciseSetById(historyExerciseSet.idHistoryExerciseSet)
        assertTrue {historyExerciseSetCacheValue == null}

    }

    @Test
    fun throwException_checkGenericError_confirmCacheUnchanged() = runBlocking {

        //Get exercise set for insertion
        val exerciseSet = exerciseSetCacheDataSource.getExerciseSetById("idExerciseSet1","idExercise1")!!


        //Create history exercise set
        val historyExerciseSet = historyExerciseSetFactory.createHistoryExerciseSet(
            idHistoryExerciseSet = FORCE_NEW_HISTORY_EXERCISE_SET_EXCEPTION,
            reps = exerciseSet.reps,
            weight = exerciseSet.weight,
            time = exerciseSet.time,
            restTime = exerciseSet.time,
            started_at = exerciseSet.startedAt,
            ended_at = exerciseSet.endedAt,
            created_at = null
        )

        //insert it
        insertHistoryExerciseSet.insertHistoryExerciseSet(
            idHistoryExerciseSet = historyExerciseSet.idHistoryExerciseSet,
            reps = historyExerciseSet.reps,
            weight = historyExerciseSet.weight,
            time = historyExerciseSet.time,
            restTime = historyExerciseSet.restTime,
            started_at = historyExerciseSet.startedAt,
            ended_at = historyExerciseSet.endedAt,
            stateEvent = InsertHistoryExerciseSetEvent(),
            historyExerciseId = "idExercise1"
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?>{
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false

                )
            }
        })

        //Check cache
        val historyExerciseSetCacheValue = historyExerciseSetCacheDataSource.getHistoryExerciseSetById(historyExerciseSet.idHistoryExerciseSet)
        assertTrue {historyExerciseSetCacheValue == null}

    }
}