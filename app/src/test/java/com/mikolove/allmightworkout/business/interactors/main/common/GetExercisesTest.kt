package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_SEARCH_EXERCISES_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.common.GetExercises.Companion.GET_EXERCISES_NO_MATCHING_RESULTS
import com.mikolove.allmightworkout.business.interactors.main.common.GetExercises.Companion.GET_EXERCISES_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.datasource.cache.database.EXERCISE_ORDER_BY_ASC_DATE_CREATED
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/*
Test cases:
1. blankQuery_success_confirmExercisesRetrieved()
    a) query with some default search options
    b) listen for GET_EXERCISES_SUCCESS emitted from flow
    c) confirm exercises were retrieved
    d) confirm exercises in cache match with exercises that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for GET_EXERCISES_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is notes in the cache
3. searchExercises_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is notes in the cache
 */
@InternalCoroutinesApi
class GetExercisesTest {

    //System in test
    private val getExercises : GetExercises

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseFactory : ExerciseFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseFactory = dependencyContainer.exerciseFactory
        getExercises = GetExercises(
            exerciseCacheDataSource
        )
    }

    @Test
    fun blankQuery_success_confirmExercisesRetrieved() = runBlocking {

        val query = ""
        var results : ArrayList<Exercise>? = null

        getExercises.execute<ExerciseViewState>(
            query = query,
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetExercisesEvent()
        ).collect(object: FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_EXERCISES_SUCCESS
                )
                value?.data?.listExercises?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // Confirm workouts retrieved
        Assertions.assertTrue{ results != null}

        // Confirm workouts in cache match with workout retrieved
        val exercisesInCache = exerciseCacheDataSource.getExercises(
            query = query,
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1)

        Assertions.assertTrue(results?.containsAll(exercisesInCache) == true)
    }


    @Test
    fun randomQuery_success_confirmNoResults() = runBlocking {

        val query = "einzeoineoinzefoizenf"
        var results: ArrayList<Exercise>? = null

        getExercises.execute<ExerciseViewState>(
            query = query,
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetExercisesEvent()
        ).collect(object: FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GET_EXERCISES_NO_MATCHING_RESULTS
                )
                value?.data?.listExercises?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // confirm nothing was retrieved
        Assertions.assertTrue { results?.run { size == 0 }?: true }

        // confirm there is notes in the cache
        val exercisesInCache = exerciseCacheDataSource.getExercises(
            query = "",
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1)
        Assertions.assertTrue { exercisesInCache.size > 0}
    }

    @Test
    fun searchExercises_fail_confirmNoResults() = runBlocking {

        val query = FORCE_SEARCH_EXERCISES_EXCEPTION
        var results: ArrayList<Exercise>? = null
        getExercises.execute<ExerciseViewState>(
            query = query,
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1,
            stateEvent = GetExercisesEvent()
        ).collect(object: FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
                value?.data?.listExercises?.let { list ->
                    results = ArrayList(list)
                }
            }
        })

        // confirm nothing was retrieved
        Assertions.assertTrue { results?.run { size == 0 }?: true }

        // confirm there is notes in the cache
        val exercisesInCache = exerciseCacheDataSource.getExercises(
            query = "",
            filterAndOrder = EXERCISE_ORDER_BY_ASC_DATE_CREATED,
            page = 1)
        Assertions.assertTrue { exercisesInCache.size > 0}
    }


}