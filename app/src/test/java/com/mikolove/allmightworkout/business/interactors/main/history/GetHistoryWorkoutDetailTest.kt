package com.mikolove.allmightworkout.business.interactors.main.history

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION
import com.mikolove.core.domain.analytics.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.analytics.GetHistoryWorkoutDetail.Companion.GET_HISTORY_WORKOUT_DETAIL_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getHistoryWorkoutDetailValidId_success_confirmWorkoutRetrieved()
    a) search for history workout with specific id
    b) listen for GET_WORKOUT_BY_ID_SUCCESS emitted from flow
    c) confirm history workout was retrieved
    d) confirm history workout in cache match with workout that was retrieved
2. getHistoryWorkoutDetailInvalidId_success_confirmNoResults()
    a) search for history workout with specific id
    b) listen for CacheErrors.CACHE_DATA_NULL emitted from flow
    c) confirm nothing was retrieved
3. getHistoryWorkoutDetail_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
 */
@InternalCoroutinesApi
class GetHistoryWorkoutDetailTest {

    //System in test
    private val getHistoryWorkoutDetail : com.mikolove.core.interactors.analytics.GetHistoryWorkoutDetail

    //Dependecies
    private val dependencyContainer : DependencyContainer
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        getHistoryWorkoutDetail = com.mikolove.core.interactors.analytics.GetHistoryWorkoutDetail(
            historyWorkoutCacheDataSource
        )
    }


    @Test
    fun getHistoryWorkoutDetailValidId_success_confirmWorkoutRetrieved() = runBlocking {

        val idHistoryWorkout = "idHistoryWorkout1"
        var historyWorkoutRetrieved : HistoryWorkout? = null

        /*
        getHistoryWorkoutDetail.getHistoryWorkoutDetail(
            idHistoryWorkout,
            stateEvent = GetHistoryWorkoutDetailEvent()
        ).collect( object :FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {

                assertEquals(
                    value?.stateMessage?.response?.message,
                    GET_HISTORY_WORKOUT_DETAIL_SUCCESS
                )

                historyWorkoutRetrieved = value?.data?.historyWorkoutDetail ?: null
            }
        })*/

        //Test
        getHistoryWorkoutDetail.getHistoryWorkoutDetail(
            idHistoryWorkout,
            stateEvent = GetHistoryWorkoutDetailEvent()
        ).collect {
            assertEquals(
                it?.message?.response?.message,
                GET_HISTORY_WORKOUT_DETAIL_SUCCESS
            )

            historyWorkoutRetrieved = it?.data?.historyWorkoutDetail ?: null
        }

        assertTrue { historyWorkoutRetrieved != null}

        val historyWorkoutInCache = historyWorkoutCacheDataSource.getHistoryWorkoutById(idHistoryWorkout)
        assertTrue( historyWorkoutRetrieved == historyWorkoutInCache)
    }

    @Test
    fun getHistoryWorkoutDetailInvalidId_success_confirmWorkoutRetrieved() = runBlocking {

        val idHistoryWorkout = "invalidId"
        var historyWorkoutRetrieved : HistoryWorkout? = null

        getHistoryWorkoutDetail.getHistoryWorkoutDetail(
            idHistoryWorkout,
            stateEvent = GetHistoryWorkoutDetailEvent()
        ).collect( object :FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_DATA_NULL) ?: false
                )

                historyWorkoutRetrieved = value?.data?.historyWorkoutDetail ?: null
            }
        })

        assertTrue { historyWorkoutRetrieved == null}

        val historyWorkoutInCache = historyWorkoutCacheDataSource.getHistoryWorkoutById(idHistoryWorkout)
        assertTrue( historyWorkoutRetrieved == historyWorkoutInCache)
    }

    @Test
    fun getHistoryWorkoutDetail_fail_confirmNoResults() = runBlocking {

        val idHistoryWorkout = FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION
        var historyWorkoutRetrieved : HistoryWorkout? = null

        getHistoryWorkoutDetail.getHistoryWorkoutDetail(
            idHistoryWorkout,
            stateEvent = GetHistoryWorkoutDetailEvent()
        ).collect( object :FlowCollector<DataState<HistoryViewState>?>{
            override suspend fun emit(value: DataState<HistoryViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )

                historyWorkoutRetrieved = value?.data?.historyWorkoutDetail ?: null
            }
        })

        assertTrue { historyWorkoutRetrieved == null}

    }

}