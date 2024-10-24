package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_EXERCISESET_EXCEPTION
import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.exercise.UpdateExerciseSet.Companion.UPDATE_EXERCISE_SET_FAILED
import com.mikolove.core.interactors.exercise.UpdateExerciseSet.Companion.UPDATE_EXERCISE_SET_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. updateExerciseSet_success_confirmNetworkAndCacheUpdated()
    a) select a random exercise set from the cache
    b) update that exercise set
    c) confirm UPDATE_EXERCISE_SET_SUCCESS msg is emitted from flow
    d) confirm exercise set is updated in network
    e) confirm exercise set is updated in cache
2. updateExerciseSet_fail_confirmNetworkAndCacheUnchanged()
    a) attempt to update a exercise set, fail since does not exist
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) attempt to update a exercise set, force an exception to throw
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
 */

@InternalCoroutinesApi
class UpdateExerciseSetTest {

    //System in test
    private val updateExerciseSet : com.mikolove.core.interactors.exercise.UpdateExerciseSet

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
        updateExerciseSet = com.mikolove.core.interactors.exercise.UpdateExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )

    }

    @Test
    fun updateExerciseSet_success_confirmNetworkAndCacheUpdated() = runBlocking {

        //Get random exercise and get Sets
        val randomExercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get first set
        val randomExerciseSet = randomExercise.sets[0]
        val updatedExerciseSet = exerciseSetFactory.createExerciseSet(
            randomExerciseSet.idExerciseSet,
            reps = 12,
            weight = 50,
            time = 120,
            restTime = 240,
            order = 1,
            created_at = randomExerciseSet.createdAt
        )

        //Upddate it
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?>
            {
                override suspend fun emit(value: DataState<ExerciseViewState>?) {
                    assertEquals(
                        value?.message?.response?.message,
                        UPDATE_EXERCISE_SET_SUCCESS
                    )
                }
            }
        )

        //Check cache updated
        val cacheSetUpdated = exerciseSetCacheDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { cacheSetUpdated == updatedExerciseSet}

        //Check network updated
        val networkSetUpdated = exerciseSetNetworkDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { networkSetUpdated == updatedExerciseSet}
    }


    @Test
    fun updateExerciseSet_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get random exercise and get Sets
        val randomExercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get first set change ID should not exist
        val randomExerciseSet = randomExercise.sets[0]
        val updatedExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_GENERAL_FAILURE,
            reps = 12,
            weight = 50,
            time = 120,
            restTime = 240,
            order = 1,
            created_at = randomExerciseSet.createdAt
        )

        //Upddate it should failed
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?>
        {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    UPDATE_EXERCISE_SET_FAILED
                )
            }
        }
        )

        //Check cache updated
        val cacheSetUpdated = exerciseSetCacheDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { cacheSetUpdated == null}

        //Check network updated
        val networkSetUpdated = exerciseSetNetworkDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { networkSetUpdated == null}

    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get random exercise and get Sets
        val randomExercise = exerciseCacheDataSource.getExercises("","",1).get(0)

        //Get first set change ID should not exist
        val randomExerciseSet = randomExercise.sets[0]
        val updatedExerciseSet = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_UPDATE_EXERCISESET_EXCEPTION,
            reps = 12,
            weight = 50,
            time = 120,
            restTime = 240,
            order = 1,
            created_at = randomExerciseSet.createdAt
        )

        //Upddate it should failed
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ExerciseViewState>?>
        {
            override suspend fun emit(value: DataState<ExerciseViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        }
        )

        //Check cache updated
        val cacheSetUpdated = exerciseSetCacheDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { cacheSetUpdated == null}

        //Check network updated
        val networkSetUpdated = exerciseSetNetworkDataSource.getExerciseSetById(updatedExerciseSet.idExerciseSet,randomExercise.idExercise)
        assertTrue { networkSetUpdated == null}


    }

}