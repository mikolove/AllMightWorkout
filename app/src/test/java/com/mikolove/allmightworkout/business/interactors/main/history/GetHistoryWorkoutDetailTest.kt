package com.mikolove.allmightworkout.business.interactors.main.history

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_HISTORY_WORKOUT_BY_ID_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.HistoryWorkout
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkoutDetail.Companion.GET_HISTORY_WORKOUT_DETAIL_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.history.state.HistoryStateEvent
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
    private val getHistoryWorkoutDetail : GetHistoryWorkoutDetail

    //Dependecies
    private val dependencyContainer : DependencyContainer
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        getHistoryWorkoutDetail = GetHistoryWorkoutDetail(
            historyWorkoutCacheDataSource
        )
    }


    @Test
    fun getHistoryWorkoutDetailValidId_success_confirmWorkoutRetrieved() = runBlocking {

        val idHistoryWorkout = "idHistoryWorkout1"
        var historyWorkoutRetrieved : HistoryWorkout? = null

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
        })

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
                    value?.stateMessage?.response?.message
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
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )

                historyWorkoutRetrieved = value?.data?.historyWorkoutDetail ?: null
            }
        })

        assertTrue { historyWorkoutRetrieved == null}

    }

}