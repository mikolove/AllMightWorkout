package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_SEARCH_WORKOUTTYPE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutType
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutTypes.Companion.GET_WORKOUTTYPES_NO_MATCHING_RESULTS
import com.mikolove.allmightworkout.business.interactors.main.common.GetWorkoutTypes.Companion.GET_WORKOUTTYPES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.WORKOUTTYPE_ORDER_BY_ASC_NAME
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/*
Test cases:
1. blankQuery_success_confirmNWorkoutsRetrieved()
    a) query with some default search options
    b) listen for GET_WORKOUTTYPES_SUCCESS emitted from flow
    c) confirm workoutTypes were retrieved
    d) confirm workoutTypes in cache match with workoutTypes that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for GET_WORKOUTTYPES_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is workoutTypes in the cache
3. searchWorkouts_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is workoutTypes in the cache
 */
@InternalCoroutinesApi
class GetWorkoutTypesTest {

    //System in test
    private val getWorkoutTypes : GetWorkoutTypes

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutTypesCacheDataSource : WorkoutTypeCacheDataSource


    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutTypesCacheDataSource = dependencyContainer.workoutTypeCacheDataSource
        getWorkoutTypes = GetWorkoutTypes(
            workoutTypesCacheDataSource
        )
    }

    @Test
    fun blankQuery_success_confirmNWorkoutsRetrieved() = runBlocking {

        //Should retrieved all values
        var query = ""
        var results : ArrayList<WorkoutType>? = null

        //Get workoutTypes
        getWorkoutTypes.execute<WorkoutViewState>(
            query = query,
            filterAndOrder = WORKOUTTYPE_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetWorkoutTypesEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_WORKOUTTYPES_SUCCESS
                )

                value?.data?.listWorkoutTypes?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm workoutType retrieved
        Assertions.assertTrue { results != null }

        //Confirm results match
        val workoutTypeInCache = workoutTypesCacheDataSource.getWorkoutTypes(query, WORKOUTTYPE_ORDER_BY_ASC_NAME,1)
        Assertions.assertTrue{ results?.containsAll(workoutTypeInCache) == true }
    }

    @Test
    fun randomQuery_success_confirmNoResults() = runBlocking {

        //Should retrieved no values
        var query = "azdadada"
        var results : ArrayList<WorkoutType>? = null

        //Get workoutTypes
        getWorkoutTypes.execute<WorkoutViewState>(
            query = query,
            filterAndOrder = WORKOUTTYPE_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetWorkoutTypesEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_WORKOUTTYPES_NO_MATCHING_RESULTS
                )

                value?.data?.listWorkoutTypes?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm nothing retrieved
        Assertions.assertTrue { results?.run { size == 0 } ?: true }

        //Confirm there workoutTypes in cache
        val workoutTypeInCache = workoutTypesCacheDataSource.getWorkoutTypes("", WORKOUTTYPE_ORDER_BY_ASC_NAME,1)
        Assertions.assertTrue{ workoutTypeInCache.size > 0 }
    }

    @Test
    fun searchWorkouts_fail_confirmNoResults() = runBlocking {

        //Should throw exception
        var query = FORCE_SEARCH_WORKOUTTYPE_EXCEPTION
        var results : ArrayList<WorkoutType>? = null

        getWorkoutTypes.execute<WorkoutViewState>(
            query = query,
            filterAndOrder = WORKOUTTYPE_ORDER_BY_ASC_NAME,
            page = 1,
            stateEvent = GetWorkoutTypesEvent()
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
                value?.data?.listWorkoutTypes?.let { list ->
                    results = ArrayList(list)
                }
            }

        })

        //Confirm nothing retrieved
        Assertions.assertTrue { results?.run { size == 0 } ?: true }

        //Confirm there workoutTypes in cache
        val workoutTypeInCache = workoutTypesCacheDataSource.getWorkoutTypes("", WORKOUTTYPE_ORDER_BY_ASC_NAME,1)
        Assertions.assertTrue{ workoutTypeInCache.size > 0 }
    }
}