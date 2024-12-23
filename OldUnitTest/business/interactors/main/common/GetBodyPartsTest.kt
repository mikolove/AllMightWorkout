package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_SEARCH_BODYPART_EXCEPTION
import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.bodypart.BodyPart
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.common.GetBodyParts.Companion.GET_BODYPARTS_NO_MATCHING_RESULTS
import com.mikolove.core.interactors.common.GetBodyParts.Companion.GET_BODYPARTS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.BODYPART_ORDER_BY_ASC_NAME
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
1. blankQuery_success_confirmNWorkoutsRetrieved()
    a) query with some default search options
    b) listen for GET_BODYPARTS_SUCCESS emitted from flow
    c) confirm bodyparts were retrieved
    d) confirm bodyparts in cache match with bodyparts that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for GET_BODYPARTS_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is bodyparts in the cache
3. searchWorkouts_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is bodyparts in the cache
 */
@InternalCoroutinesApi
class GetBodyPartsTest {

    //System in test
    private val getBodyParts : com.mikolove.core.interactors.common.GetBodyParts

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        getBodyParts = com.mikolove.core.interactors.common.GetBodyParts(
            bodyPartCacheDataSource
        )
    }

    @Test
    fun blankQuery_success_confirmNWorkoutsRetrieved() = runBlocking {

        //Should retrieved all values
        var query = ""
        var results : ArrayList<BodyPart>? = null

        //Get bodyparts
        getBodyParts.getBodyParts<WorkoutViewState>(
            query = query,
            filterAndOrder = BODYPART_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetBodyPartEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assertEquals(
                    value?.message?.response?.message,
                    GET_BODYPARTS_SUCCESS
                )

                value?.data?.listBodyParts?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm bodyPart retrieved
        assertTrue { results != null }

        //Confirm results match
        val bodyPartInCache = bodyPartCacheDataSource.getBodyParts(query, BODYPART_ORDER_BY_ASC_NAME,1)
        assertTrue{ results?.containsAll(bodyPartInCache) == true }
    }

    @Test
    fun randomQuery_success_confirmNoResults() = runBlocking {

        //Should retrieved no values
        var query = "azdadada"
        var results : ArrayList<BodyPart>? = null

        //Get bodyparts
        getBodyParts.getBodyParts<WorkoutViewState>(
            query = query,
            filterAndOrder = BODYPART_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetBodyPartEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assertEquals(
                    value?.message?.response?.message,
                    GET_BODYPARTS_NO_MATCHING_RESULTS
                )

                value?.data?.listBodyParts?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm nothing retrieved
        assertTrue { results?.run { size == 0 } ?: true }

        //Confirm there isbodyParts in cache
        val bodyPartInCache = bodyPartCacheDataSource.getBodyParts("", BODYPART_ORDER_BY_ASC_NAME,1)
        assertTrue{ bodyPartInCache.size > 0 }
    }

    @Test
    fun searchWorkouts_fail_confirmNoResults() = runBlocking {

        //Should throw exception
        var query = FORCE_SEARCH_BODYPART_EXCEPTION
        var results : ArrayList<BodyPart>? = null

        getBodyParts.getBodyParts<WorkoutViewState>(
            query = query,
            filterAndOrder = BODYPART_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetBodyPartEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
                value?.data?.listBodyParts?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm nothing retrieved
        assertTrue { results?.run { size == 0 } ?: true }

        //Confirm there isbodyParts in cache
        val bodyPartInCache = bodyPartCacheDataSource.getBodyParts("", BODYPART_ORDER_BY_ASC_NAME,1)
        assertTrue{ bodyPartInCache.size > 0 }
    }
}