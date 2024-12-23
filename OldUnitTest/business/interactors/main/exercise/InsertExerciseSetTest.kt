package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_EXERCISESET_EXCEPTION
import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertExerciseSet_success_confirmNetworkAndCacheUpdated()
    a) insert a new exercise set
    b) listen for INSERT_EXERCISE_SET_SUCCESS emission from flow
    c) confirm cache was updated with new exercise set
    d) confirm network was updated with new exercise set
2. insertExerciseSet_fail_confirmNetworkAndCacheUnchanged()
    a) insert a new exercise set
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_EXERCISE_SET_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) insert a new exercise set
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
 */

@InternalCoroutinesApi
class InsertExerciseSetTest {

    private val insertExerciseSet : com.mikolove.core.interactors.exercise.InsertExerciseSet

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private val exerciseSetNetworkDataSource : ExerciseSetNetworkDataSource
    private val exerciseSetFactory : ExerciseSetFactory

    init {

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        exerciseSetFactory = dependencyContainer.exerciseSetFactory

        insertExerciseSet = com.mikolove.core.interactors.exercise.InsertExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource,
            exerciseSetFactory
        )
    }

    @Test
    fun insertExerciseSet_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val idExercise = "idExercise1"
        val newExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = UUID.randomUUID().toString(),
            reps = null,
            weight = null,
            time = null,
            restTime = null,
            created_at = null
        )

        insertExerciseSet.execute(
            idExerciseSet = newExerciseSet.idExerciseSet,
            reps = newExerciseSet.reps,
            weight = newExerciseSet.weight,
            time = newExerciseSet.time,
            restTime = newExerciseSet.restTime,
            order = newExerciseSet.order,
            idExercise = idExercise,
            stateEvent = InsertExerciseSetEvent(exerciseSet = newExerciseSet,idExercise = idExercise)
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    com.mikolove.core.interactors.exercise.InsertExerciseSet.INSERT_EXERCISE_SET_SUCCESS
                )

            }
        })


        //Confirm cache was updated
        val cacheExerciseSetThatWasInserted = exerciseSetCacheDataSource.getExerciseSetById(newExerciseSet.idExerciseSet,idExercise)
        Assertions.assertTrue { cacheExerciseSetThatWasInserted == newExerciseSet }

        //Confirm network was updated
        val networkExerciseSetThatWasInserted = exerciseSetNetworkDataSource.getExerciseSetById(newExerciseSet.idExerciseSet,idExercise)
        Assertions.assertTrue { networkExerciseSetThatWasInserted == newExerciseSet }

    }

    @Test
    fun insertExerciseSet_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        val idExercise = "idExercise1"
        val newExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_GENERAL_FAILURE,
            reps = null,
            weight = null,
            time = null,
            restTime = null,
            created_at = null
        )

        insertExerciseSet.execute(
            idExerciseSet = newExerciseSet.idExerciseSet,
            reps = newExerciseSet.reps,
            weight = newExerciseSet.weight,
            time = newExerciseSet.time,
            restTime = newExerciseSet.restTime,
            order = newExerciseSet.order,
            idExercise = idExercise,
            stateEvent = InsertExerciseSetEvent(exerciseSet = newExerciseSet,idExercise = idExercise)
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    com.mikolove.core.interactors.exercise.InsertExerciseSet.INSERT_EXERCISE_SET_FAILED
                )

            }
        })


        //Confirm cache was updated
        val cacheExerciseSetThatWasInserted = exerciseSetCacheDataSource.getExerciseSetById(newExerciseSet.idExerciseSet,idExercise)
        Assertions.assertTrue { cacheExerciseSetThatWasInserted == null }

        //Confirm network was updated
        val networkExerciseSetThatWasInserted = exerciseSetNetworkDataSource.getExerciseSetById(newExerciseSet.idExerciseSet,idExercise)
        Assertions.assertTrue { networkExerciseSetThatWasInserted == null }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val idExercise = "idExercise1"
        val newExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_NEW_EXERCISESET_EXCEPTION,
            reps = null,
            weight = null,
            time = null,
            restTime = null,
            created_at = null
        )

        insertExerciseSet.execute(
            idExerciseSet = newExerciseSet.idExerciseSet,
            reps = newExerciseSet.reps,
            weight = newExerciseSet.weight,
            time = newExerciseSet.time,
            restTime = newExerciseSet.restTime,
            order = newExerciseSet.order,
            idExercise = idExercise,
            stateEvent = InsertExerciseSetEvent(exerciseSet = newExerciseSet,idExercise = idExercise)
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })


        //Confirm cache was updated
        val cacheExerciseSetThatWasInserted =
            exerciseSetCacheDataSource.getExerciseSetById(newExerciseSet.idExerciseSet, idExercise)
        Assertions.assertTrue { cacheExerciseSetThatWasInserted == null }

        //Confirm network was updated
        val networkExerciseSetThatWasInserted = exerciseSetNetworkDataSource.getExerciseSetById(
            newExerciseSet.idExerciseSet,
            idExercise
        )
        Assertions.assertTrue { networkExerciseSetThatWasInserted == null }

    }
}
