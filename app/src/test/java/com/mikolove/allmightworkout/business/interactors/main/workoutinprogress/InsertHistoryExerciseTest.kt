package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_HISTORY_EXERCISE_EXCEPTION
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExercise.Companion.INSERT_HISTORY_EXERCISE_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExercise.Companion.INSERT_HISTORY_EXERCISE_SUCCESS
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
1. insertHistoryExercise_success_confirmCacheUpdated()
    a) insert a new history exercise
    b) listen for INSERT_HISTORY_EXERCISE_SUCCESS emission from flow
    c) confirm cache was updated with new history exercise
2. insertHistoryExercise_failed_confirmCacheUnchanged()
    a) insert a new history exercise
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_HISTORY_EXERCISE_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError_confirmCacheUnchanged()
    a) insert a new history exercise
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */

@InternalCoroutinesApi
class InsertHistoryExerciseTest {

    //System in test
    private val insertHistoryExercise : InsertHistoryExercise

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val historyExerciseCacheDataSource : HistoryExerciseCacheDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val workoutTypeCacheDataSource : WorkoutTypeCacheDataSource
    private val historyExerciseFactory : HistoryExerciseFactory

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        historyExerciseCacheDataSource = dependencyContainer.historyExerciseCacheDataSource
        workoutTypeCacheDataSource = dependencyContainer.workoutTypeCacheDataSource
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        historyExerciseFactory = dependencyContainer.historyExerciseFactory
        insertHistoryExercise =InsertHistoryExercise(
            historyExerciseCacheDataSource,
            historyExerciseFactory
        )
    }

    @Test
    fun insertHistoryExercise_success_confirmCacheUpdated() = runBlocking {

        //Get exercise for insertion
        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val workoutType = workoutTypeCacheDataSource.getWorkoutTypeBydBodyPartId(exercise.bodyPart?.idBodyPart)

        exercise.start(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)
        exercise.stop(dependencyContainer.dateUtil.getCurrentTimestamp())
        delay(1000)

        //Create history workout
        val historyExercise = historyExerciseFactory.createHistoryExercise(
            idHistoryExercise = UUID.randomUUID().toString(),
            name = exercise.name,
            bodyPart = exercise.bodyPart?.name,
            workoutType = workoutType?.name,
            exerciseType = exercise.exerciseType.name,
            historySets = null,
            started_at = exercise.startedAt,
            ended_at = exercise.endedAt,
            created_at = null
        )

        //Insert history exercise
        insertHistoryExercise.insertHistoryExercise(
            idHistoryExercise = historyExercise.idHistoryExercise,
            name = historyExercise.name,
            bodyPart = historyExercise.bodyPart,
            workoutType = historyExercise.workoutType,
            exerciseType = historyExercise.exerciseType,
            started_at = historyExercise.startedAt,
            ended_at = historyExercise.endedAt,
            idHistoryWorkout = UUID.randomUUID().toString(),
            stateEvent = InsertHistoryExerciseEvent()
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?> {
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_HISTORY_EXERCISE_SUCCESS
                )
            }
        })

        //Confirm cache updated
        var historyExerciseCacheValue = historyExerciseCacheDataSource.getHistoryExerciseById(historyExercise.idHistoryExercise)
        assertTrue { historyExerciseCacheValue == historyExercise }
    }

    @Test
    fun insertHistoryExercise_failed_confirmCacheUnchanged() = runBlocking {

        //Get exercise for insertion
        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val workoutType = workoutTypeCacheDataSource.getWorkoutTypeBydBodyPartId(exercise.bodyPart?.idBodyPart)

        //Create history workout
        val historyExercise = historyExerciseFactory.createHistoryExercise(
            idHistoryExercise = FORCE_GENERAL_FAILURE,
            name = exercise.name,
            bodyPart = exercise.bodyPart?.name,
            workoutType = workoutType?.name,
            exerciseType = exercise.exerciseType.name,
            historySets = null,
            started_at = exercise.startedAt,
            ended_at = exercise.endedAt,
            created_at = null
        )

        //Insert history exercise
        insertHistoryExercise.insertHistoryExercise(
            idHistoryExercise = historyExercise.idHistoryExercise,
            name = historyExercise.name,
            bodyPart = historyExercise.bodyPart,
            workoutType = historyExercise.workoutType,
            exerciseType = historyExercise.exerciseType,
            started_at = historyExercise.startedAt,
            ended_at = historyExercise.endedAt,
            idHistoryWorkout = UUID.randomUUID().toString(),
            stateEvent = InsertHistoryExerciseEvent()
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?> {
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_HISTORY_EXERCISE_FAILED
                )
            }
        })

        //Confirm cache updated
        var historyExerciseCacheValue = historyExerciseCacheDataSource.getHistoryExerciseById(historyExercise.idHistoryExercise)
        assertTrue { historyExerciseCacheValue == null }
    }

    @Test
    fun throwException_checkGenericError_confirmCacheUnchanged() = runBlocking {
        //Get exercise for insertion
        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val workoutType = workoutTypeCacheDataSource.getWorkoutTypeBydBodyPartId(exercise.bodyPart?.idBodyPart)

        //Create history workout
        val historyExercise = historyExerciseFactory.createHistoryExercise(
            idHistoryExercise = FORCE_NEW_HISTORY_EXERCISE_EXCEPTION,
            name = exercise.name,
            bodyPart = exercise.bodyPart?.name,
            workoutType = workoutType?.name,
            exerciseType = exercise.exerciseType.name,
            historySets = null,
            started_at = exercise.startedAt,
            ended_at = exercise.endedAt,
            created_at = null
        )

        //Insert history exercise
        insertHistoryExercise.insertHistoryExercise(
            idHistoryExercise = historyExercise.idHistoryExercise,
            name = historyExercise.name,
            bodyPart = historyExercise.bodyPart,
            workoutType = historyExercise.workoutType,
            exerciseType = historyExercise.exerciseType,
            started_at = historyExercise.startedAt,
            ended_at = historyExercise.endedAt,
            idHistoryWorkout = UUID.randomUUID().toString(),
            stateEvent = InsertHistoryExerciseEvent()
        ).collect( object : FlowCollector<DataState<WorkoutInProgressViewState>?> {
            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Confirm cache updated
        var historyExerciseCacheValue = historyExerciseCacheDataSource.getHistoryExerciseById(historyExercise.idHistoryExercise)
        assertTrue { historyExerciseCacheValue == null }

    }
}