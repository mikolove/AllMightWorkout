package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_EXERCISESET_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateMultipleExerciseSet.Companion.UPDATE_EXERCISE_SETS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.exercise.UpdateMultipleExerciseSet.Companion.UPDATE_EXERCISE_SETS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


/*
Test cases:
1. updateMultipleExerciseSet_success_confirmNetworkAndCacheUpdated()
    a) select a handful of random sets for updating
    b) update from cache and network
    c) confirm UPDATE_EXERCISE_SETS_SUCCESS msg is emitted from flow
    d) confirm sets are updated from cache
    e) confirm sets are updated from in network
2. updateMultipleExerciseSet_fail_confirmCorrectUpdateMade()
    a) select a handful of random sets for updating
    b) change the ids of a few sets so they will cause errors when updating
    c) confirm UPDATE_EXERCISE_SETS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid sets are updated from network
    e) confirm ONLY the valid sets are updated from cache
    f) confirm invalid exercises are not updated from cache
3. updateMultipleExerciseSet_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) select a handful of random sets for updating
    b) force an exception to be thrown on one of them
    c) confirm UPDATE_EXERCISE_SETS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid sets are deleted from network
    e) confirm ONLY the valid sets are deleted from cache
 */

@InternalCoroutinesApi
class UpdateMultipleExerciseSetTest {

    //System in test
    private var updateMultipleExerciseSet : UpdateMultipleExerciseSet

    //Dependencies
    private var dependencyContainer: DependencyContainer
    private var exerciseCacheDataSource : ExerciseCacheDataSource
    private var exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private var exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    private var dateUtil : DateUtil
    init {

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        dateUtil = dependencyContainer.dateUtil
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        updateMultipleExerciseSet = UpdateMultipleExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )
    }

    @Test
    fun updateMultipleExerciseSet_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets
        val updatedSets : ArrayList<ExerciseSet> = ArrayList()

        //Update some sets
        sets.forEach { set ->
            val updateSet = set.copy(
                reps = 15,
                weight = 15,
                restTime = 120,
                order = 1,
                time = 100,
                updatedAt = dateUtil.getCurrentTimestamp()
            )
            updatedSets.add(updateSet)
        }

        updateMultipleExerciseSet.updateMultipleExerciseSet(
            updatedSets,
            exercise.idExercise,
            stateEvent = ExerciseStateEvent.UpdateMultipleExerciseSetEvent(updatedSets,exercise.idExercise)
        ).collect { value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                UPDATE_EXERCISE_SETS_SUCCESS
            )
        }

        //Check cache
        updatedSets.forEach { set ->
            val setInCache = exerciseSetCacheDataSource.getExerciseSetById(set.idExerciseSet,exercise.idExercise)
            assertTrue { updatedSets.contains(setInCache) }
        }

        //Check network
        updatedSets.forEach { set ->
            val setInNetwork = exerciseSetNetworkDataSource.getExerciseSetById(set.idExerciseSet,exercise.idExercise)
            assertTrue { updatedSets.contains(setInNetwork) }
        }
    }

    @Test
    fun updateMultipleExerciseSet_fail_confirmCorrectUpdateMade() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets
        val updatedSets : ArrayList<ExerciseSet> = ArrayList()

        //Update some sets
        //Make invalid the second one
        sets.forEachIndexed { index, set ->

            val idSet = if(index == 2) "invalidIdSet" else set.idExerciseSet
            val updateSet = set.copy(
                idExerciseSet = idSet,
                reps = 15,
                weight = 15,
                restTime = 120,
                order = 1,
                time = 100,
                updatedAt = dateUtil.getCurrentTimestamp()
            )
            updatedSets.add(updateSet)
        }

        updateMultipleExerciseSet.updateMultipleExerciseSet(
            updatedSets,
            exercise.idExercise,
            stateEvent = ExerciseStateEvent.UpdateMultipleExerciseSetEvent(updatedSets,exercise.idExercise)
        ).collect { value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                UPDATE_EXERCISE_SETS_ERRORS
            )
        }

        //Check cache only valid updated
        updatedSets.forEachIndexed { index, set ->

            val setInCache = exerciseSetCacheDataSource.getExerciseSetById(set.idExerciseSet,exercise.idExercise)
            if(index == 2){
                assertTrue { setInCache == null }
            }else{
                assertTrue { updatedSets.contains(setInCache) }
            }
        }

        //Check network only valid updated
        updatedSets.forEachIndexed{ index, set ->

            val setInNetwork = exerciseSetNetworkDataSource.getExerciseSetById(set.idExerciseSet, exercise.idExercise)
            if(index == 2){
                assertTrue { setInNetwork == null }
            }else {
                assertTrue { updatedSets.contains(setInNetwork) }
            }
        }
    }

    @Test
    fun updateMultipleExerciseSet_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets
        val updatedSets : ArrayList<ExerciseSet> = ArrayList()

        //Update some sets
        //Make exception thrown by the second one
        sets.forEachIndexed { index, set ->

            val idSet = if(index == 2) FORCE_UPDATE_EXERCISESET_EXCEPTION else set.idExerciseSet
            val updateSet = set.copy(
                idExerciseSet = idSet,
                reps = 15,
                weight = 15,
                restTime = 120,
                order = 1,
                time = 100,
                updatedAt = dateUtil.getCurrentTimestamp()
            )
            updatedSets.add(updateSet)
        }

        updateMultipleExerciseSet.updateMultipleExerciseSet(
            updatedSets,
            exercise.idExercise,
            stateEvent = ExerciseStateEvent.UpdateMultipleExerciseSetEvent(updatedSets,exercise.idExercise)
        ).collect { value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                UPDATE_EXERCISE_SETS_ERRORS
            )
        }

        //Check cache only valid updated
        updatedSets.forEachIndexed { index, set ->

            val setInCache = exerciseSetCacheDataSource.getExerciseSetById(set.idExerciseSet,exercise.idExercise)
            if(index == 2){
                assertTrue { setInCache == null }
            }else{
                assertTrue { updatedSets.contains(setInCache) }
            }
        }

        //Check network only valid updated
        updatedSets.forEachIndexed{ index, set ->

            val setInNetwork = exerciseSetNetworkDataSource.getExerciseSetById(set.idExerciseSet, exercise.idExercise)
            if(index == 2){
                assertTrue { setInNetwork == null }
            }else {
                assertTrue { updatedSets.contains(setInNetwork) }
            }
        }
    }

}