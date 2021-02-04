package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_HISTORY_EXERCISE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryExerciseFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryExercise.Companion.INSERT_HISTORY_EXERCISE_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryExercise.Companion.INSERT_HISTORY_EXERCISE_SUCCESS
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
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
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
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
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
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Confirm cache updated
        var historyExerciseCacheValue = historyExerciseCacheDataSource.getHistoryExerciseById(historyExercise.idHistoryExercise)
        assertTrue { historyExerciseCacheValue == null }

    }
}