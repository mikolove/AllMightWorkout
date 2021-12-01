package com.mikolove.allmightworkout.business.interactors.main.common

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_EXERCISE_BY_ID_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


/*
Test cases:
1. getExerciseByValidId_success_confirmExerciseRetrieved()
    a) search for exercise with specific id
    b) listen for GET_EXERCISE_BY_ID_SUCCESS emitted from flow
    c) confirm exercise was retrieved
    d) confirm exercise in cache match with workout that was retrieved
2. getExerciseByInvalidId_success_confirmNoResults()
    a) search for exercise with specific id
    b) listen for CacheErrors.CACHE_DATA_NULL emitted from flow
    c) confirm nothing was retrieved
3. getExerciseById_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
 */

@InternalCoroutinesApi
class GetExerciseByIdTest {

    //System in test
    private val getExerciseById : GetExerciseById

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        getExerciseById = GetExerciseById(
            exerciseCacheDataSource
        )
    }

    @Test
    fun getExerciseByValidId_success_confirmExerciseRetrieved() = runBlocking {

        //Exercise id
        val idExercise = "idExercise1"
        var exerciseRetrieved : Exercise? = null

        //Search it
        getExerciseById.execute<ExerciseViewState>(
            idExercise = idExercise,
            stateEvent = ExerciseStateEvent.GetExerciseByIdEvent(idExercise = idExercise)
        ).collect( object  : FlowCollector<DataState<ExerciseViewState>?> {

            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                Assertions.assertEquals(
                    value?.message?.response?.message,
                    GetExerciseById.GET_EXERCISE_BY_ID_SUCCESS
                )

                exerciseRetrieved = value?.data?.exerciseSelected ?: null
            }
        })

        //Confirm workout retrieved
        Assertions.assertTrue { exerciseRetrieved != null}

        //Confirm workout match with cache
        val exerciseInCache = exerciseCacheDataSource.getExerciseById(idExercise)
        Assertions.assertTrue{ exerciseRetrieved == exerciseInCache}

    }

    @Test
    fun getWorkoutByInvalidId_success_confirmNoResults() = runBlocking {

        //Exercise id
        val idExercise = "invalidIdExercise"
        var exerciseRetrieved : Exercise? = null

        //Search it
        getExerciseById.execute<ExerciseViewState>(
            idExercise = idExercise,
            stateEvent = ExerciseStateEvent.GetExerciseByIdEvent(idExercise = idExercise)
        ).collect( object  : FlowCollector<DataState<ExerciseViewState>?> {

            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_DATA_NULL) ?: false)

                exerciseRetrieved = value?.data?.exerciseSelected ?: null
            }
        })

        //Confirm workout retrieved is null
        Assertions.assertTrue { exerciseRetrieved == null}

        //Confirm workout is not in cache
        val exerciseInCache = exerciseCacheDataSource.getExerciseById(idExercise)
        Assertions.assertTrue{ exerciseRetrieved == exerciseInCache}

    }

    @Test
    fun getExerciseById_fail_confirmNoResults() = runBlocking {

        //Exercise id
        val idExercise = FORCE_GET_EXERCISE_BY_ID_EXCEPTION
        var exerciseRetrieved : Exercise? = null

        //Search it
        getExerciseById.execute<ExerciseViewState>(
            idExercise = idExercise,
            stateEvent = ExerciseStateEvent.GetExerciseByIdEvent(idExercise = idExercise)
        ).collect( object  : FlowCollector<DataState<ExerciseViewState>?> {

            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false)

                exerciseRetrieved = value?.data?.exerciseSelected ?: null
            }
        })

        //Confirm workout retrieved is null
        Assertions.assertTrue { exerciseRetrieved == null}
    }
}