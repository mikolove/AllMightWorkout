package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_SEARCH_WORKOUTS_EXCEPTION
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.workout.GetWorkouts.Companion.GET_WORKOUTS_NO_MATCHING_RESULTS
import com.mikolove.core.interactors.workout.GetWorkouts.Companion.GET_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUT_ORDER_BY_ASC_DATE_CREATED
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import com.mikolove.core.interactors.workout.GetWorkouts
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. blankQuery_success_confirmWorkoutsRetrieved()
    a) query with some default search options
    b) listen for GET_WORKOUTS_SUCCESS emitted from flow
    c) confirm workouts were retrieved
    d) confirm workouts in cache match with workouts that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for GET_WORKOUTS_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is workouts in the cache
3. searchWorkouts_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is workouts in the cache
 */
@InternalCoroutinesApi
class GetWorkoutsTest {

    //System in test
    private val getWorkouts: com.mikolove.core.interactors.workout.GetWorkouts

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutFactory : WorkoutFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutFactory = dependencyContainer.workoutFactory
        getWorkouts = com.mikolove.core.interactors.workout.GetWorkouts(
            workoutCacheDataSource
        )
    }


    @Test
    fun blankQuery_success_confirmNWorkoutsRetrieved() = runBlocking {

        val query = ""
        var results : ArrayList<Workout>? = null

        getWorkouts.execute(
            query = query,
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetWorkoutsEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assertEquals(
                    value?.message?.response?.message,
                    GET_WORKOUTS_SUCCESS
                )
                value?.data?.listWorkouts?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // Confirm workouts retrieved
        assertTrue{ results != null}

        // Confirm workouts in cache match with workout retrieved
        val workoutInCache = workoutCacheDataSource.getWorkouts(
            query = query,
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1
        )

        assertTrue(results?.containsAll(workoutInCache) == true)
    }


    @Test
    fun randomQuery_success_confirmNoResults() = runBlocking {

        val query = "einzeoineoinzefoizenf"
        var results: ArrayList<Workout>? = null
        getWorkouts.execute(
            query = query,
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetWorkoutsEvent()
        ).collect(object: FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    GET_WORKOUTS_NO_MATCHING_RESULTS
                )
                value?.data?.listWorkouts?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // confirm nothing was retrieved
        assertTrue { results?.run { size == 0 }?: true }

        // confirm there is notes in the cache
        val workoutInCache = workoutCacheDataSource.getWorkouts(
            query = "",
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1
        )
        assertTrue { workoutInCache.size > 0}
    }

    @Test
    fun searchWorkouts_fail_confirmNoResults() = runBlocking {

        val query = FORCE_SEARCH_WORKOUTS_EXCEPTION
        var results: ArrayList<Workout>? = null
        getWorkouts.execute(
            query = query,
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetWorkoutsEvent()
        ).collect(object: FlowCollector<DataState<WorkoutViewState>?>{
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
                value?.data?.listWorkouts?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // confirm nothing was retrieved
        assertTrue { results?.run { size == 0 }?: true }

        // confirm there is notes in the cache
        val workoutsInCache = workoutCacheDataSource.getWorkouts(
            query = "",
            filterAndOrder = WORKOUT_ORDER_BY_ASC_DATE_CREATED,
            page = 1
        )
        assertTrue { workoutsInCache.size > 0}
    }

}