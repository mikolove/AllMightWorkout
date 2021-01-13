package com.mikolove.allmightworkout.business.interactors.main.manageexercise

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_EXERCISE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource

import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.UpdateExercise.Companion.UPDATE_EXERCISE_FAILED
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.UpdateExercise.Companion.UPDATE_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. updateExercise_success_confirmNetworkAndCacheUpdated()
    a) select a random exercise from the cache
    b) update that exercise
    c) confirm UPDATE_EXERCISE_SUCCESS msg is emitted from flow
    d) confirm exercise is updated in network
    e) confirm exercise is updated in cache
2. updateExercise_fail_confirmNetworkAndCacheUnchanged()
    a) attempt to update a exercise, fail since does not exist
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) attempt to update a exercise, force an exception to throw
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
 */


@InternalCoroutinesApi
class UpdateWorkoutTest {

    private val updateExercise : UpdateExercise

    //Dependencies
    private val dependencyContainer: DependencyContainer
    private val exerciseCacheDataSource: ExerciseCacheDataSource
    private val exerciseNetworkDataSource: ExerciseNetworkDataSource
    private val bodyPartCacheDataSource : BodyPartCacheDataSource

    private val exerciseFactory: ExerciseFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        exerciseFactory = dependencyContainer.exerciseFactory
        updateExercise = UpdateExercise(
            exerciseCacheDataSource,
            exerciseNetworkDataSource
        )
    }

    @Test
    fun updateExercise_success_confirmNetworkAndCacheUpdated() = runBlocking {

        //Get random exercise
        val randomExercise = exerciseCacheDataSource.getExercises("","", 1).get(0)

        //Get new bodyPart
        val updateBodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart2")

        //Change exercise
        val updatedExercise = exerciseFactory.createExercise(
            idExercise = randomExercise.idExercise,
            name = "new name",
            sets = null,
            bodyPart = updateBodyPart!!,
            exerciseType = ExerciseType.TIME_EXERCISE,
            isActive = false,
            created_at = randomExercise.created_at
        )

        //Update it
        updateExercise.updateExercise(
            exercise = updatedExercise,
            stateEvent = UpdateExerciseEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?> {

            override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    UPDATE_EXERCISE_SUCCESS
                )
            }

        })

        //Confirm cache updated
        val cacheExerciseUpdated = exerciseCacheDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { cacheExerciseUpdated == updatedExercise}

        //Confirm network updated
        val networkExerciseUpdated = exerciseNetworkDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { networkExerciseUpdated == updatedExercise}

    }

    @Test
    fun updateExercise_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get random exercise
        val randomExercise = exerciseCacheDataSource.getExercises("","", 1).get(0)

        //Get new bodyPart
        val updateBodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart2")

        //Change exercise
        val updatedExercise = exerciseFactory.createExercise(
            idExercise = FORCE_GENERAL_FAILURE,
            name = "new name",
            sets = null,
            bodyPart = updateBodyPart!!,
            exerciseType = ExerciseType.TIME_EXERCISE,
            isActive = false,
            created_at = randomExercise.created_at
        )

        //Update it
        updateExercise.updateExercise(
            exercise = updatedExercise,
            stateEvent = UpdateExerciseEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?> {

            override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    UPDATE_EXERCISE_FAILED
                )
            }

        })

        //Confirm cache updated
        val cacheExerciseUpdated = exerciseCacheDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { cacheExerciseUpdated == null}

        //Confirm network updated
        val networkExerciseUpdated = exerciseNetworkDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { networkExerciseUpdated == null}
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get random exercise
        val randomExercise = exerciseCacheDataSource.getExercises("","", 1).get(0)

        //Get new bodyPart
        val updateBodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart2")

        //Change exercise
        val updatedExercise = exerciseFactory.createExercise(
            idExercise = FORCE_UPDATE_EXERCISE_EXCEPTION,
            name = "new name",
            sets = null,
            bodyPart = updateBodyPart!!,
            exerciseType = ExerciseType.TIME_EXERCISE,
            isActive = false,
            created_at = randomExercise.created_at
        )

        //Update it
        updateExercise.updateExercise(
            exercise = updatedExercise,
            stateEvent = UpdateExerciseEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?> {

            override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }

        })

        //Confirm cache updated
        val cacheExerciseUpdated = exerciseCacheDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { cacheExerciseUpdated == null}

        //Confirm network updated
        val networkExerciseUpdated = exerciseNetworkDataSource.getExerciseById(updatedExercise.idExercise)
        assertTrue { networkExerciseUpdated == null}

    }
}