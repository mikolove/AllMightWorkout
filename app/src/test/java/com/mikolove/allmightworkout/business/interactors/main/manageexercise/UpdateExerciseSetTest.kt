package com.mikolove.allmightworkout.business.interactors.main.manageexercise

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_EXERCISESET_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.UpdateExerciseSet.Companion.UPDATE_EXERCISE_SET_FAILED
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.UpdateExerciseSet.Companion.UPDATE_EXERCISE_SET_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.manageexercise.state.ManageExerciseViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

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
    private val updateExerciseSet : UpdateExerciseSet

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
        updateExerciseSet = UpdateExerciseSet(
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
            created_at = randomExerciseSet.created_at
        )

        //Upddate it
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?>
            {
                override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                    assertEquals(
                        value?.stateMessage?.response?.message,
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
            created_at = randomExerciseSet.created_at
        )

        //Upddate it should failed
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?>
        {
            override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
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
            created_at = randomExerciseSet.created_at
        )

        //Upddate it should failed
        updateExerciseSet.updateExerciseSet(
            updatedExerciseSet,
            randomExercise.idExercise,
            stateEvent = UpdateExerciseSetEvent()
        ).collect( object : FlowCollector<DataState<ManageExerciseViewState>?>
        {
            override suspend fun emit(value: DataState<ManageExerciseViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
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