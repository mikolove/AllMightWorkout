package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_EXERCISE_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertExercise_success_confirmNetworkAndCacheUpdated()
    a) insert a new exercise
    b) listen for INSERT_EXERCISE_SUCCESS emission from flow
    c) confirm cache was updated with new exercise
    d) confirm network was updated with new exercise
2. insertExercise_fail_confirmNetworkAndCacheUnchanged()
    a) insert a new exercise
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_EXERCISE_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) insert a new exercise
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
 */

@InternalCoroutinesApi
class InsertExerciseTest {

    private val insertExercise : InsertExercise

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val bodyPartCacheDataSource : BodyPartCacheDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource
    private val exerciseFactory : ExerciseFactory

    init {

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        exerciseFactory = dependencyContainer.exerciseFactory

        bodyPartCacheDataSource = dependencyContainer.bodyPartCacheDataSource

        insertExercise = InsertExercise(
            exerciseCacheDataSource,
            exerciseNetworkDataSource,
            exerciseFactory)
    }

    @Test
    fun insertExercise_success_confirmNetworkAndCacheUpdated() = runBlocking {

        //Get BodyPart
        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")

        //Get Exercise Type
        val exerciseType = ExerciseType.REP_EXERCISE

        val newExercise = exerciseFactory.createExercise(
            idExercise = null,
            name = UUID.randomUUID().toString(),
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = exerciseType,
            created_at = null
        )

        insertExercise.insertExercise(
            idExercise = newExercise.idExercise,
            name = newExercise.name,
            exerciseType = exerciseType,
            bodyPart = bodyPart!!,
            stateEvent = InsertExerciseEvent(name = newExercise.name, bodyPart = newExercise.bodyPart!! ,exerciseType = newExercise.exerciseType),
            sets = null
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?>{
            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    InsertExercise.INSERT_EXERCISE_SUCCESS
                )

            }
        })

        //Confirm cache was updated
        val cacheExerciseThatWasInserted = exerciseCacheDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { cacheExerciseThatWasInserted == newExercise }

        //Confirm network was updated
        val networkExerciseThatWasInserted = exerciseNetworkDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { networkExerciseThatWasInserted == newExercise }

    }


    @Test
    fun insertExercise_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get BodyPart
        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")

        //Get Exercise Type
        val exerciseType = ExerciseType.REP_EXERCISE

        val newExercise = exerciseFactory.createExercise(
            idExercise = FORCE_GENERAL_FAILURE,
            name = UUID.randomUUID().toString(),
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = exerciseType,
            created_at = null
        )

        insertExercise.insertExercise(
            idExercise = newExercise.idExercise,
            name = newExercise.name,
            exerciseType = exerciseType,
            bodyPart = bodyPart!!,
            stateEvent = InsertExerciseEvent(name = newExercise.name, bodyPart = newExercise.bodyPart!! ,exerciseType = newExercise.exerciseType),
            sets = null
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?>{
            override suspend fun emit(value: DataState<ExerciseViewState>?) {

                Assertions.assertEquals(
                    value?.stateMessage?.response?.message,
                    InsertExercise.INSERT_EXERCISE_FAILED
                )

            }
        })

        //Confirm cache was updated
        val cacheExerciseThatWasInserted = exerciseCacheDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { cacheExerciseThatWasInserted == null }

        //Confirm network was updated
        val networkExerciseThatWasInserted = exerciseNetworkDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { networkExerciseThatWasInserted == null }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get BodyPart
        val bodyPart = bodyPartCacheDataSource.getBodyPartById("idBodyPart1")

        //Get Exercise Type
        val exerciseType = ExerciseType.REP_EXERCISE

        val newExercise = exerciseFactory.createExercise(
            idExercise = FORCE_NEW_EXERCISE_EXCEPTION,
            name = UUID.randomUUID().toString(),
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = exerciseType,
            created_at = null
        )

        insertExercise.insertExercise(
            idExercise = newExercise.idExercise,
            name = newExercise.name,
            exerciseType = exerciseType,
            bodyPart = bodyPart!!,
            stateEvent = InsertExerciseEvent(name = newExercise.name, bodyPart = newExercise.bodyPart!! ,exerciseType = newExercise.exerciseType),
            sets = null
        ).collect(object : FlowCollector<DataState<ExerciseViewState>?>{
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN)  ?: false
                )

            }
        })

        //Confirm cache was updated
        val cacheExerciseThatWasInserted = exerciseCacheDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { cacheExerciseThatWasInserted == null }

        //Confirm network was updated
        val networkExerciseThatWasInserted = exerciseNetworkDataSource.getExerciseById(newExercise.idExercise)
        Assertions.assertTrue { networkExerciseThatWasInserted == null }
    }

}
