package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GET_EXERCISE_SET_BY_ID_EXERCISE_EXCEPTION
import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise.Companion.GET_EXERCISE_SET_BY_ID_EXERCISE_NO_RESULT
import com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise.Companion.GET_EXERCISE_SET_BY_ID_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getExerciseSetByIdExerciseValidId_success_confirmRetrieved()
    a) search for sets with specific id
    b) listen for GET_EXERCISE_SET_BY_ID_EXERCISE_SUCCESS emitted from flow
    c) confirm sets was retrieved
    d) confirm sets in cache match with sets that was retrieved
2. getExerciseSetByIdExerciseInvalidId_success_confirmNoResults()
    a) search for sets with specific id
    b) listen for GET_EXERCISE_SET_BY_ID_EXERCISE_NO_RESULT emitted from flow
    c) confirm nothing was retrieved
3. getExerciseSetByIdExercise_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
 */

@InternalCoroutinesApi
class GetExerciseSetByIdExerciseTest {

    private val getExerciseSetByIdExercise : com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise

    private val dependencyContainer : DependencyContainer
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource

    init {

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        getExerciseSetByIdExercise =
            com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise(
                exerciseSetCacheDataSource
            )
    }

    @Test
    fun getExerciseSetByIdExerciseValidId_success_confirmRetrieved() = runBlocking {

        val idExercise = "idExercise1"
        var setsRetrieved : ArrayList<ExerciseSet>? = null

        getExerciseSetByIdExercise.getExerciseSetByIdExercise(
            idExercise = idExercise,
            stateEvent = GetExerciseSetByIdExerciseEvent(
                idExercise = idExercise
            )
        ).collect{ value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                GET_EXERCISE_SET_BY_ID_EXERCISE_SUCCESS
            )

            value?.data?.cachedExerciseSetsByIdExercise?.let { sets ->
                setsRetrieved = ArrayList(sets)
            }
        }

        assertTrue { setsRetrieved != null}

        val setsInCache = exerciseSetCacheDataSource.getExerciseSetByIdExercise(idExercise)
        assertTrue { setsRetrieved?.containsAll(setsInCache) == true}
    }

    @Test
    fun getExerciseSetByIdExerciseInvalidId_success_confirmNoResults() = runBlocking {

        val idExercise = "idExerciseInvalid"
        var setsRetrieved : ArrayList<ExerciseSet>? = null

        getExerciseSetByIdExercise.getExerciseSetByIdExercise(
            idExercise = idExercise,
            stateEvent = GetExerciseSetByIdExerciseEvent(
                idExercise = idExercise
            )
        ).collect{ value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                GET_EXERCISE_SET_BY_ID_EXERCISE_NO_RESULT
            )

            value?.data?.cachedExerciseSetsByIdExercise?.let { sets ->
                setsRetrieved = ArrayList(sets)
            }
        }

        assertTrue { setsRetrieved?.run { size == 0 }?: true}
    }

    @Test
    fun getExerciseSetByIdExercise_fail_confirmNoResults() = runBlocking {

        val idExercise = FORCE_GET_EXERCISE_SET_BY_ID_EXERCISE_EXCEPTION

        var setsRetrieved : ArrayList<ExerciseSet>? = null

        getExerciseSetByIdExercise.getExerciseSetByIdExercise(
            idExercise = idExercise,
            stateEvent = GetExerciseSetByIdExerciseEvent(
                idExercise = idExercise
            )
        ).collect{ value ->

            assert(
                value?.message?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false)

            value?.data?.cachedExerciseSetsByIdExercise?.let { sets ->
                setsRetrieved = ArrayList(sets)
            }
        }

        assertTrue { setsRetrieved?.run { size == 0 }?: true}
    }

}