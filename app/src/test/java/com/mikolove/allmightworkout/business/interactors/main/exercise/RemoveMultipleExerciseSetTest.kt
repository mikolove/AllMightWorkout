package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_EXERCISESET_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExerciseSet.Companion.DELETE_EXERCISE_SETS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.exercise.RemoveMultipleExerciseSet.Companion.DELETE_EXERCISE_SETS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/*
Test cases:
1. deleteMultipleExerciseSet_success_confirmNetworkAndCacheUpdated()
    a) select a handful of random sets for delete
    b) delete from cache and network
    c) confirm DELETE_EXERCISE_SETS_SUCCESS msg is emitted from flow
    d) confirm sets are deleted from cache
    e) confirm sets are deleted from in network
2. deleteMultipleExerciseSet_fail_confirmCorrectUpdateMade()
    a) select a handful of random sets for updating
    b) change the ids of a few sets so they will cause errors when deleted
    c) confirm DELETE_EXERCISE_SETS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid sets are deleted from network
    e) confirm ONLY the valid sets are deleted from cache
    f) confirm invalid exercises are not deleted from cache
3. deleteMultipleExerciseSet_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) select a handful of random sets for updating
    b) force an exception to be thrown on one of them
    c) confirm DELETE_EXERCISE_SETS_ERRORS msg is emitted from flow
    d) confirm ONLY the valid sets are deleted from network
    e) confirm ONLY the valid sets are deleted from cache
 */

class RemoveMultipleExerciseSetTest {

    //System in test
    private var removeMultipleExerciseSet : RemoveMultipleExerciseSet? = null

    //Dependencies
    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var exerciseCacheDataSource : ExerciseCacheDataSource
    private lateinit var exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private lateinit var exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    private lateinit var dateUtil : DateUtil
    init {


    }

    @AfterEach
    fun afterEach(){
        removeMultipleExerciseSet = null
    }

    @BeforeEach
    fun beforeEach(){
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        dateUtil = dependencyContainer.dateUtil
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        removeMultipleExerciseSet = RemoveMultipleExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )
    }

    @Test
    fun deleteMultipleExerciseSet_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets

        removeMultipleExerciseSet?.removeMultipleExerciseSet(
            sets,
            idExercise = exercise.idExercise,
            stateEvent = RemoveMultipleExerciseSetEvent(ArrayList(sets),exercise.idExercise)
        )?.collect{ value ->
            assertEquals(
                value?.message?.response?.message,
                DELETE_EXERCISE_SETS_SUCCESS
            )
        }

        val totalInCache = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue {totalInCache == 0}

        val totalInNetwork = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue { totalInNetwork == 0 }
    }

    @Test
    fun deleteMultipleExerciseSet_fail_confirmCorrectUpdateMade() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets
        val updateSets : ArrayList<ExerciseSet> = ArrayList()

        val totalInCacheBefore = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        val totalInNetworkBefore = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        sets.forEachIndexed{ index, set ->
            if(index == 2){
                val updateSet = set.copy(
                    idExerciseSet = "invalidId"
                )
                updateSets.add(updateSet)
            }else{
                updateSets.add(set)
            }
        }

        removeMultipleExerciseSet?.removeMultipleExerciseSet(
            updateSets,
            idExercise = exercise.idExercise,
            stateEvent = RemoveMultipleExerciseSetEvent(ArrayList(sets),exercise.idExercise)
        )?.collect{ value ->
            assertEquals(
                value?.message?.response?.message,
                DELETE_EXERCISE_SETS_ERRORS
            )
        }

        val totalInCache = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue { totalInCache < totalInCacheBefore}

        val totalInNetwork = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue { totalInNetwork < totalInNetworkBefore}

    }

    @Test
    fun deleteMultipleExerciseSet_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val exercise = exerciseCacheDataSource.getExerciseById("idExercise1")!!
        val sets = exercise.sets
        val updateSets : ArrayList<ExerciseSet> = ArrayList()

        val totalInCacheBefore = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        val totalInNetworkBefore = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)

        sets.forEachIndexed{ index, set ->
            if(index == 2){
                val updateSet = set.copy(
                    idExerciseSet = FORCE_DELETE_EXERCISESET_EXCEPTION
                )
                updateSets.add(updateSet)
            }else{
                updateSets.add(set)
            }
        }

        removeMultipleExerciseSet?.removeMultipleExerciseSet(
            updateSets,
            idExercise = exercise.idExercise,
            stateEvent = RemoveMultipleExerciseSetEvent(ArrayList(sets),exercise.idExercise)
        )?.collect{ value ->
            assertEquals(
                value?.message?.response?.message,
                DELETE_EXERCISE_SETS_ERRORS
            )
        }

        val totalInCache = exerciseSetCacheDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue { totalInCache < totalInCacheBefore}

        val totalInNetwork = exerciseSetNetworkDataSource.getTotalExercisesSetByExercise(exercise.idExercise)
        assertTrue { totalInNetwork < totalInNetworkBefore}

    }
}