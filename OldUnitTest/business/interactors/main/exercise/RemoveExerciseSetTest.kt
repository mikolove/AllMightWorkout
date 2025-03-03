package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_EXERCISESET_EXCEPTION
import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.exercise.RemoveExerciseSet.Companion.DELETE_EXERCISE_SET_FAILED
import com.mikolove.core.interactors.exercise.RemoveExerciseSet.Companion.DELETE_EXERCISE_SET_SUCCESS
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
1. deleteExerciseSet_success_confirmNetworkUpdated()
    a) delete an exercise set
    b) check for success message from flow emission
    c) confirm exercise set was deleted from network
    d) confirm exercise set was deleted from cache
2. deleteExerciseSet_fail_confirmNetworkUnchanged()
    a) attempt to delete an exercise set, fail since does not exist
    b) check for failure message from flow emission
    c) confirm network was not changed
3. throwException_checkGenericError_confirmNetworkUnchanged()
    a) attempt to delete an exercise set, force an exception to throw
    b) check for failure message from flow emission
    c) confirm network was not changed
 */

@InternalCoroutinesApi
class RemoveExerciseSetTest {

    //System in test
    private val removeExerciseSet : com.mikolove.core.interactors.exercise.RemoveExerciseSet

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private val exerciseSetNetworkDataSource : ExerciseSetNetworkDataSource
    private val exerciseSetFactory : ExerciseSetFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        exerciseSetFactory = dependencyContainer.exerciseSetFactory
        removeExerciseSet = com.mikolove.core.interactors.exercise.RemoveExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )
    }

    @Test
    fun deleteExerciseSet_success_confirmNetworkUpdated() = runBlocking {

        //Get exercise
        val exercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get first set
        val exerciseSet = exercise.sets?.get(0)

        //Remove it
        removeExerciseSet.removeExerciseSet(
            exerciseSet = exerciseSet,
            idExercise = exercise.idExercise,
            stateEvent = RemoveExerciseSetEvent(exerciseId = exercise.idExercise)
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    DELETE_EXERCISE_SET_SUCCESS
                )
            }
        })

        //Check exercise deleted from cache
        val cacheRemoveExerciseSet = exerciseSetCacheDataSource.getExerciseSetById(exerciseSet.idExerciseSet,exercise.idExercise)
        Assertions.assertTrue { cacheRemoveExerciseSet == null }

        //Check exercise deleted from network
        val networkRemoveExerciseSet = exerciseSetNetworkDataSource.getExerciseSetById(exerciseSet.idExerciseSet,exercise.idExercise)
        Assertions.assertTrue { networkRemoveExerciseSet == null }

    }

    @Test
    fun deleteExerciseSet_fail_confirmNetworkUnchanged() = runBlocking {

        //Get exercise
        val exercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get Total Exercises
        val beforeExercisesSetTotal = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        //Get first set
        val exerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = UUID.randomUUID().toString(),
            reps = 1,
            weight = 1,
            time =1,
            restTime = 1,
            created_at = null
        )

        //Remove it
        removeExerciseSet.removeExerciseSet(
            exerciseSet = exerciseSet,
            idExercise = exercise.idExercise,
            stateEvent = RemoveExerciseSetEvent(exerciseId = exercise.idExercise)
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                Assertions.assertEquals(
                    value?.message?.response?.message,
                    DELETE_EXERCISE_SET_FAILED
                )
            }
        })

        //Get Total Exercises
        val afterExercisesSetTotal  = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        val afterExercisesSetNetworkTotal  = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        //Check total in cache unchanged
        Assertions.assertTrue { beforeExercisesSetTotal == afterExercisesSetTotal }

        //Check total in network unchanged
        Assertions.assertTrue { afterExercisesSetNetworkTotal == afterExercisesSetTotal }

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking {

        val exercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get Total Exercises
        val beforeExercisesSetTotal = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        //Get first set
        val exerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_DELETE_EXERCISESET_EXCEPTION,
            reps = 1,
            weight = 1,
            time =1,
            restTime = 1,
            created_at = null
        )

        //Remove it
        removeExerciseSet.removeExerciseSet(
            exerciseSet = exerciseSet,
            idExercise = exercise.idExercise,
            stateEvent = RemoveExerciseSetEvent(exerciseId = exercise.idExercise)
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?> {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.message?.response?.message
                            ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Get Total Exercises
        val afterExercisesSetTotal  = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        val afterExercisesSetNetworkTotal  = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        //Check total in cache unchanged
        Assertions.assertTrue { beforeExercisesSetTotal == afterExercisesSetTotal }

        //Check total in network unchanged
        Assertions.assertTrue { afterExercisesSetNetworkTotal == afterExercisesSetTotal }
    }

}