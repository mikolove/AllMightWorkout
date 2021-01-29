package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_HISTORY_WORKOUT_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryWorkout.Companion.INSERT_HISTORY_WORKOUT_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryWorkout.Companion.INSERT_HISTORY_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
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
1. insertHistoryWorkout_success_confirmCacheUpdated()
    a) insert a new history workout
    b) listen for INSERT_HISTORY_WORKOUT_SUCCESS emission from flow
    c) confirm cache was updated with new history workout
2. insertHistoryWorkout_failed_confirmCacheUnchanged()
    a) insert a new history workout
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_HISTORY_WORKOUT_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError_confirmCacheUnchanged()
    a) insert a new history workout
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */


@InternalCoroutinesApi
class InsertHistoryWorkoutTest {

    //System in test
    private val insertHistoryWorkout : InsertHistoryWorkout

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val historyWorkoutFactory : HistoryWorkoutFactory

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        historyWorkoutFactory = dependencyContainer.historyWorkoutFactory
        insertHistoryWorkout =InsertHistoryWorkout(
            historyWorkoutCacheDataSource,
            historyWorkoutFactory
        )
    }

    @Test
    fun insertHistoryWorkout_success_confirmCacheUpdated() = runBlocking {

        //Get workout for insertion
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!
        workout.start(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)
        workout.stop(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)

        //Create history workout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = UUID.randomUUID().toString(),
            name = workout.name,
            historyExercises = null,
            started_at = workout.startedAt,
            ended_at = workout.endedAt,
            created_at = null)

        //Insert history workout
        insertHistoryWorkout.insertHistoryWorkout(
            idHistoryWorkout = historyWorkout.idHistoryWorkout,
            name = historyWorkout.name,
            started_at = historyWorkout.startedAt,
            ended_at = historyWorkout.endedAt,
            stateEvent = InsertHistoryWorkoutEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    INSERT_HISTORY_WORKOUT_SUCCESS
                )
            }
        })

        //Confirm cache updated
        var historyWorkoutCacheValue =historyWorkoutCacheDataSource.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)
        assertTrue { historyWorkoutCacheValue == historyWorkout }
    }

    @Test
    fun insertHistoryWorkout_failed_confirmCacheUnchanged() = runBlocking {

        //Get workout for insertion
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Create history workout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = FORCE_GENERAL_FAILURE,
            name = workout.name,
            historyExercises = null,
            started_at = workout.startedAt,
            ended_at = workout.endedAt,
            created_at = null)

        //Insert history workout
        insertHistoryWorkout.insertHistoryWorkout(
            idHistoryWorkout = historyWorkout.idHistoryWorkout,
            name = historyWorkout.name,
            started_at = historyWorkout.startedAt,
            ended_at = historyWorkout.endedAt,
            stateEvent = InsertHistoryWorkoutEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    INSERT_HISTORY_WORKOUT_FAILED
                )
            }
        })

        //Confirm cache updated
        var historyWorkoutCacheValue =historyWorkoutCacheDataSource.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)
        assertTrue { historyWorkoutCacheValue == null}
    }

    @Test
    fun throwException_checkGenericError_confirmCacheUnchanged() = runBlocking {

        //Get workout for insertion
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Create history workout
        val historyWorkout = historyWorkoutFactory.createHistoryWorkout(
            idHistoryWorkout = FORCE_NEW_HISTORY_WORKOUT_EXCEPTION,
            name = workout.name,
            historyExercises = null,
            started_at = workout.startedAt,
            ended_at = workout.endedAt,
            created_at = null)

        //Insert history workout
        insertHistoryWorkout.insertHistoryWorkout(
            idHistoryWorkout = historyWorkout.idHistoryWorkout,
            name = historyWorkout.name,
            started_at = historyWorkout.startedAt,
            ended_at = historyWorkout.endedAt,
            stateEvent = InsertHistoryWorkoutEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Confirm cache updated
        var historyWorkoutCacheValue =historyWorkoutCacheDataSource.getHistoryWorkoutById(historyWorkout.idHistoryWorkout)
        assertTrue { historyWorkoutCacheValue == null}
    }
}