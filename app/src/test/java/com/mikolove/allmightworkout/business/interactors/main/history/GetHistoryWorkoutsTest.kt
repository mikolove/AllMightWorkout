package com.mikolove.allmightworkout.business.interactors.main.history

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_SEARCH_HISTORY_WORKOUTS_EXCEPTION
import com.mikolove.core.domain.analytics.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.analytics.GetHistoryWorkouts.Companion.GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS
import com.mikolove.core.interactors.analytics.GetHistoryWorkouts.Companion.GET_HISTORY_WORKOUTS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.HISTORY_WORKOUT_ORDER_BY_ASC_NAME
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryViewState
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
    b) listen for GET_HISTORY_WORKOUTS_SUCCESS emitted from flow
    c) confirm history workouts were retrieved
    d) confirm history workouts in cache match with workouts that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is history workouts in the cache
3. searchWorkouts_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is history workouts in the cache
 */

@InternalCoroutinesApi
class GetHistoryWorkoutsTest {

    //System in test
    private val getHistoryWorkouts : com.mikolove.core.interactors.analytics.GetHistoryWorkouts

    //Dependecies
    private val dependencyContainer : DependencyContainer
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        getHistoryWorkouts = com.mikolove.core.interactors.analytics.GetHistoryWorkouts(
            historyWorkoutCacheDataSource
        )
    }

    @Test
    fun blankQuery_success_confirmWorkoutsRetrieved() = runBlocking {

        val query = ""
        var results : ArrayList<HistoryWorkout>? = null

        getHistoryWorkouts.getHistoryWorkouts(
            query,
            filterAndOrder = HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            page =1,
            stateEvent = GetHistoryWorkoutsEvent()
        ).collect( object : FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    GET_HISTORY_WORKOUTS_SUCCESS
                )
                value?.data?.listHistoryWorkouts?.let {
                    results = ArrayList(it)
                }
            }
        })

        //confirm retrieved
        assertTrue{ results != null}

        //Confirm cache match
        val historyWorkoutInCache = historyWorkoutCacheDataSource.getHistoryWorkouts(
            query,
            HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            1
        )

        assertTrue(results?.containsAll(historyWorkoutInCache) == true)
    }

    @Test
    fun randomQuery_success_confirmNoResults() = runBlocking {

        val query = "randomquery"
        var results : ArrayList<HistoryWorkout>? = null

        getHistoryWorkouts.getHistoryWorkouts(
            query,
            filterAndOrder = HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            page =1,
            stateEvent = GetHistoryWorkoutsEvent()
        ).collect( object : FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    GET_HISTORY_WORKOUTS_NO_MATCHING_RESULTS
                )
                value?.data?.listHistoryWorkouts?.let {
                    results = ArrayList(it)
                }
            }
        })

        //confirm retrieved
        assertTrue{ results?.run{ size ==0 } ?: true }

        //Confirm cache not empty
        val historyWorkoutInCache = historyWorkoutCacheDataSource.getHistoryWorkouts(
            "",
            HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            1
        )

        assertTrue( historyWorkoutInCache.size >0 )

    }

    @Test
    fun searchWorkouts_fail_confirmNoResults() = runBlocking {

        val query = FORCE_SEARCH_HISTORY_WORKOUTS_EXCEPTION
        var results : ArrayList<HistoryWorkout>? = null

        getHistoryWorkouts.getHistoryWorkouts(
            query,
            filterAndOrder = HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            page =1,
            stateEvent = GetHistoryWorkoutsEvent()
        ).collect( object : FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
                value?.data?.listHistoryWorkouts?.let {
                    results = ArrayList(it)
                }
            }
        })

        //confirm retrieved
        assertTrue{ results?.run{ size ==0 } ?: true }

        //Confirm cache not empty
        val historyWorkoutInCache = historyWorkoutCacheDataSource.getHistoryWorkouts(
            "",
            HISTORY_WORKOUT_ORDER_BY_ASC_NAME,
            1
        )

        assertTrue( historyWorkoutInCache.size >0 )
    }
}