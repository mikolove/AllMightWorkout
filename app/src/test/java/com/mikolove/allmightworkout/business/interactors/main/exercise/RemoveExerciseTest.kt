package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_EXERCISE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.exercise.usecase.RemoveExercise.Companion.DELETE_EXERCISE_FAILED
import com.mikolove.core.domain.exercise.usecase.RemoveExercise.Companion.DELETE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import com.mikolove.core.domain.exercise.usecase.RemoveExercise
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. deleteExercise_success_confirmNetworkUpdated()
    a) delete a exercise
    b) check for success message from flow emission
    c) confirm exercise was deleted from network
    d) confirm exercise was deleted from cache
2. deleteExercise_fail_confirmNetworkUnchanged()
    a) attempt to delete a exercise, fail since does not exist
    b) check for failure message from flow emission
    c) confirm network was not changed
3. throwException_checkGenericError_confirmNetworkUnchanged()
    a) attempt to delete a exercise, force an exception to throw
    b) check for failure message from flow emission
    c) confirm network was not changed
 */

@InternalCoroutinesApi
class RemoveExerciseTest {

    //System in test
    private val removeExercise : RemoveExercise<ExerciseViewState>

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource
    private val bodyPartCacheDataSource : BodyPartCacheDataSource
    private val exerciseFactory : ExerciseFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        exerciseFactory = dependencyContainer.exerciseFactory
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        removeExercise = RemoveExercise(
            exerciseCacheDataSource,
            exerciseNetworkDataSource
        )
    }


    @Test
    fun deleteExercise_success_confirmNetworkUpdated() = runBlocking {

        //Get exercise
        val removedExercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Remove it
        removeExercise.removeExercise(
            exercise = removedExercise,
            stateEvent = RemoveExerciseEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    DELETE_EXERCISE_SUCCESS
                )
            }
        })

        //Check exercise deleted from cache
        val cacheRemoveExercise = exerciseCacheDataSource.getExerciseById(removedExercise.idExercise)
        println(cacheRemoveExercise)
        assertTrue { cacheRemoveExercise == null}

        //Check exercise deleted from network
        val networkRemoveExercise = exerciseNetworkDataSource.getExerciseById(removedExercise.idExercise)
        println(networkRemoveExercise)
        assertTrue { networkRemoveExercise == null}

    }

    @Test
    fun deleteExercise_fail_confirmNetworkUnchanged() = runBlocking {

        //Create invalid exercise
        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")

        val exerciseToDelete = exerciseFactory.createExercise(
            idExercise = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString(),
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = ExerciseType.TIME_EXERCISE,
            created_at = null
        )

        //Get Total Exercises
        val beforeExercisesTotal = exerciseCacheDataSource.getTotalExercises()

        //Remove it
        removeExercise.removeExercise(
            exercise = exerciseToDelete,
            stateEvent = RemoveExerciseEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    DELETE_EXERCISE_FAILED
                )
            }
        })

        //Get Total Exercises
        val afterExercisesTotal = exerciseCacheDataSource.getTotalExercises()
        val afterExercisesNetworkTotal = exerciseNetworkDataSource.getTotalExercises()

        //Check total in cache unchanged
        assertTrue { beforeExercisesTotal == afterExercisesTotal }

        //Check total in network unchanged
        assertTrue { afterExercisesNetworkTotal == afterExercisesTotal }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking {

        //Create invalid exercise
        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")

        val exerciseToDelete = exerciseFactory.createExercise(
            idExercise = FORCE_DELETE_EXERCISE_EXCEPTION,
            name = UUID.randomUUID().toString(),
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = ExerciseType.TIME_EXERCISE,
            created_at = null
        )

        //Get Total Exercises
        val beforeExercisesTotal = exerciseCacheDataSource.getTotalExercises()

        //Remove it
        removeExercise.removeExercise(
            exercise = exerciseToDelete,
            stateEvent = RemoveExerciseEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Get Total Exercises
        val afterExercisesTotal = exerciseCacheDataSource.getTotalExercises()
        val afterExercisesNetworkTotal = exerciseNetworkDataSource.getTotalExercises()

        //Check total in cache unchanged
        assertTrue { beforeExercisesTotal == afterExercisesTotal }

        //Check total in network unchanged
        assertTrue { afterExercisesNetworkTotal == afterExercisesTotal }
    }
}