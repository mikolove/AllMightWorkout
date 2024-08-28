package com.mikolove.allmightworkout.business.interactors.main.workoutinprogress

import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workoutinprogress.state.WorkoutInProgressViewState
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.data.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.exercise.Exercise
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.data.workouttype.abstraction.WorkoutTypeCacheDataSource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. insertHistory_success_confirmCacheUpdated()
    a) insert a new history
    b) listen for INSERT_HISTORY_SUCCESS emission from flow
    c) confirm cache was updated with new history
2. insertHistory_failed_confirmCacheUnchanged()
    a) insert a new history
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_HISTORY_FAILED emission from flow
    e) confirm cache was not updated
3. throwException_checkGenericError_confirmCacheUnchanged()
    a) insert a new history
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
 */

@InternalCoroutinesApi
class InsertHistoryTest {

    //System in test
    private val insertHistory : com.mikolove.core.interactors.workoutinprogress.InsertHistory

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val dateUtil : DateUtil

    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource
    private val historyExerciseCacheDataSource : HistoryExerciseCacheDataSource
    private val historyExerciseSetCacheDataSource : HistoryExerciseSetCacheDataSource
    private val workoutTypeCacheDataSource : WorkoutTypeCacheDataSource

    private val workoutFactory : WorkoutFactory
    private val exerciseFactory : ExerciseFactory
    private val exerciseSetFactory : ExerciseSetFactory

    private val historyWorkoutFactory : HistoryWorkoutFactory
    private val historyExerciseFactory : HistoryExerciseFactory
    private val historyExerciseSetFactory : HistoryExerciseSetFactory

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        dateUtil = dependencyContainer.dateUtil

        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        historyExerciseCacheDataSource = dependencyContainer.historyExerciseCacheDataSource
        historyExerciseSetCacheDataSource = dependencyContainer.historyExerciseSetCacheDataSource
        workoutTypeCacheDataSource = dependencyContainer.workoutTypeCacheDataSource

        workoutFactory = dependencyContainer.workoutFactory
        exerciseFactory = dependencyContainer.exerciseFactory
        exerciseSetFactory = dependencyContainer.exerciseSetFactory

        historyWorkoutFactory = dependencyContainer.historyWorkoutFactory
        historyExerciseFactory = dependencyContainer.historyExerciseFactory
        historyExerciseSetFactory = dependencyContainer.historyExerciseSetFactory

        insertHistory = com.mikolove.core.interactors.workoutinprogress.InsertHistory(
            historyWorkoutCacheDataSource,
            historyExerciseCacheDataSource,
            historyExerciseSetCacheDataSource,
            workoutTypeCacheDataSource,
            historyWorkoutFactory,
            historyExerciseFactory,
            historyExerciseSetFactory
        )
    }


    @Test
    fun insertHistory_success_confirmCacheUpdated() = runBlocking {

        val workout = generateWorkoutWithExerciseAndSet()
        var idHistoryWorkout : String? = ""

        val totalSize = historyExerciseCacheDataSource.getTotalHistoryExercise()

        insertHistory.execute(
            workout = workout,
            stateEvent = InsertHistoryEvent(workout)
        ).collect(object : FlowCollector<DataState<WorkoutInProgressViewState>?>{

            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    com.mikolove.core.interactors.workoutinprogress.InsertHistory.INSERT_HISTORY_SUCCESS
                )

                idHistoryWorkout = value?.data?.idHistoryWorkoutInserted
            }
        })

        val historyWorkout = idHistoryWorkout?.let {
            historyWorkoutCacheDataSource.getHistoryWorkoutById(it)
        }

        assertTrue { historyWorkout != null }

        //Dependency container and json loaded are outdated those test should be cleaned later
        val historyExercisesSize = historyExerciseCacheDataSource.getTotalHistoryExercise()
        assertTrue{ historyExercisesSize - totalSize == 2}
    }


    @Test
    fun insertHistory_failed_confirmCacheUnchanged() = runBlocking {


        val workout = generateWorkoutWithExerciseAndSet()
        var idHistoryWorkout : String? = ""

        insertHistory.execute(
            workout = workout,
            idHistoryWorkout = FORCE_GENERAL_FAILURE,
            stateEvent = InsertHistoryEvent(workout)
        ).collect(object : FlowCollector<DataState<WorkoutInProgressViewState>?>{

            override suspend fun emit(value: DataState<WorkoutInProgressViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    com.mikolove.core.interactors.workoutinprogress.InsertHistory.INSERT_HISTORY_FAILED
                )

                idHistoryWorkout = value?.data?.idHistoryWorkoutInserted
            }
        })

        assertTrue{ idHistoryWorkout == null }
    }


    private suspend fun generateWorkoutWithExerciseAndSet() : Workout {

        val workout = workoutFactory.createWorkout(
            idWorkout = null,
            name = "Test Workout",
            exercises = null,
            isActive = true,
            created_at = null
        )

        workout.startedAt = dateUtil.getCurrentTimestamp()

        val exercises : ArrayList<Exercise> = ArrayList()
        repeat(2){ i ->

            val exercise = exerciseFactory.createExercise(
                idExercise = null,
                name = "Name ${i}",
                sets = null,
                bodyPart = null,
                exerciseType = ExerciseType.REP_EXERCISE,
                isActive = true,
                created_at = dateUtil.getCurrentTimestamp()
            )
            exercise.startedAt = dateUtil.getCurrentTimestamp()

            val sets  : ArrayList<ExerciseSet> = ArrayList()

            repeat(2){ j ->
                val set = exerciseSetFactory.createExerciseSet(
                    idExerciseSet = null,
                    reps = null,
                    weight = null,
                    time = null,
                    restTime = null,
                    created_at = dateUtil.getCurrentTimestamp()
                )

                set.createdAt = dateUtil.getCurrentTimestamp()
                set.startedAt  =dateUtil.getCurrentTimestamp()
                delay(1000)
                set.endedAt = dateUtil.getCurrentTimestamp()

                sets.add(set)
            }

            exercise.sets = sets

            delay(1000)
            exercise.endedAt = dateUtil.getCurrentTimestamp()
            exercises.add(exercise)
        }

        workout.exercises = exercises
        workout.endedAt = dateUtil.getCurrentTimestamp()

        return workout
    }

}