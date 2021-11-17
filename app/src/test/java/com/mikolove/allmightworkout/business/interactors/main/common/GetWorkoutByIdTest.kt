package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_WORKOUT_BY_ID_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutById.Companion.GET_WORKOUT_BY_ID_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getWorkoutByValidId_success_confirmWorkoutRetrieved()
    a) search for workout with specific id
    b) listen for GET_WORKOUT_BY_ID_SUCCESS emitted from flow
    c) confirm workout was retrieved
    d) confirm workout in cache match with workout that was retrieved
2. getWorkoutByInvalidId_success_confirmNoResults()
    a) search for workout with specific id
    b) listen for CacheErrors.CACHE_DATA_NULL emitted from flow
    c) confirm nothing was retrieved
3. getWorkoutById_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
 */
@InternalCoroutinesApi
class GetWorkoutByIdTest {

    //System in test
    private val getWorkoutById : GetWorkoutById

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource =dependencyContainer.workoutCacheDataSource
        getWorkoutById = GetWorkoutById(
            workoutCacheDataSource
        )
    }

    @Test
    fun getWorkoutByValidId_success_confirmWorkoutRetrieved() = runBlocking {

        //Workout id
        val idWorkout = "idWorkout1"
        var workoutRetrieved : Workout? = null

        //Search it
        getWorkoutById.execute<WorkoutViewState>(
            idWorkout = idWorkout,
            stateEvent = GetWorkoutByIdEvent(idWorkout)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assertEquals(
                    value?.message?.response?.message,
                    GET_WORKOUT_BY_ID_SUCCESS
                )

                workoutRetrieved = value?.data?.workoutSelected ?: null
            }
        })

        //Confirm workout retrieved
        assertTrue { workoutRetrieved != null}

        //Confirm workout match with cache
        val workoutInCache = workoutCacheDataSource.getWorkoutById(idWorkout)
        assertTrue{ workoutRetrieved == workoutInCache}

    }

    @Test
    fun getWorkoutByInvalidId_success_confirmNoResults() = runBlocking {

        //Workout id invalid
        val idWorkout = "invalidWorkoutId"
        var workoutRetrieved : Workout? = null

        //Search it
        getWorkoutById.execute<WorkoutViewState>(
            idWorkout = idWorkout,
            stateEvent = GetWorkoutByIdEvent(idWorkout)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_DATA_NULL) ?: false)

                workoutRetrieved = value?.data?.workoutSelected ?: null
            }
        })

        //Confirm workout retrieved is null
        assertTrue { workoutRetrieved == null}

        //Confirm workout is not in cache
        val workoutInCache = workoutCacheDataSource.getWorkoutById(idWorkout)
        assertTrue{ workoutRetrieved == workoutInCache}

    }

    @Test
    fun getWorkoutById_fail_confirmNoResults() = runBlocking {

        //Workout id invalid
        val idWorkout = FORCE_GET_WORKOUT_BY_ID_EXCEPTION
        var workoutRetrieved : Workout? = null

        //Search it
        getWorkoutById.execute<WorkoutViewState>(
            idWorkout = idWorkout,
            stateEvent = GetWorkoutByIdEvent(idWorkout)
        ).collect( object  : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false)

                workoutRetrieved = value?.data?.workoutSelected ?: null
            }
        })

        //Confirm workout retrieved is null
        assertTrue { workoutRetrieved == null}
    }
}