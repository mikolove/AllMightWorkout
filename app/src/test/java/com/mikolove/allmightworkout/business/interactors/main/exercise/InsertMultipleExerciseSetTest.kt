package com.mikolove.allmightworkout.business.interactors.main.exercise

import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_EXERCISESET_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseSet
import com.mikolove.allmightworkout.business.domain.model.ExerciseSetFactory
import com.mikolove.allmightworkout.business.interactors.main.exercise.InsertMultipleExerciseSet.Companion.ADD_EXERCISE_SETS_ERRORS
import com.mikolove.allmightworkout.business.interactors.main.exercise.InsertMultipleExerciseSet.Companion.ADD_EXERCISE_SETS_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.exercise.state.ExerciseStateEvent.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList


/*
Test cases:
1. insertMultipleExerciseSet_success_confirmNetworkAndCacheUpdated()
    a) insert exercise sets
    b) listen for ADD_EXERCISE_SETS_SUCCESS emission from flow
    c) confirm cache was updated with new exercise set
    d) confirm network was updated with new exercise set
2. insertMultipleExerciseSet_errors_confirmNetworkAndCacheUpdated()
    a) insert exercise sets
    b) force a failure (return -1 from db operation)
    c) listen for ADD_EXERCISE_SETS_ERRORS emission from flow
    e) confirm cache was updated only with success
    e) confirm network was updated only with success
 */


@InternalCoroutinesApi
class InsertMultipleExerciseSetTest {

    private var insertMultipleExerciseSet: InsertMultipleExerciseSet? = null

    //Dependencies
    private lateinit var dependencyContainer: DependencyContainer

    private lateinit var exerciseSetCacheDataSource: ExerciseSetCacheDataSource
    private lateinit var exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    private lateinit var exerciseSetFactory: ExerciseSetFactory


    @AfterEach
    fun afterEach(){
        insertMultipleExerciseSet = null
    }

    @BeforeEach
    fun beforeEach(){

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        exerciseSetFactory = dependencyContainer.exerciseSetFactory

        insertMultipleExerciseSet = InsertMultipleExerciseSet(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )
    }

    @Test
    fun insertMultipleExerciseSet_success_confirmNetworkAndCacheUpdated() = runBlocking {

        val idExercise = "idExercise1"
        val sets : ArrayList<ExerciseSet> = ArrayList()
        repeat(3){
            val newExerciseSet = exerciseSetFactory.createExerciseSet(
                idExerciseSet = UUID.randomUUID().toString(),
                reps = null,
                weight = null,
                time = null,
                restTime = null,
                created_at = null
            )
            sets.add(newExerciseSet)
        }

        //Insert
        insertMultipleExerciseSet?.insertMultipleExerciseSet(
            sets,
            idExercise,
            InsertMultipleExerciseSetEvent(sets, idExercise)
        )?.collect { value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                ADD_EXERCISE_SETS_SUCCESS
            )
        }

        val setsInCache = exerciseSetCacheDataSource.getExerciseSetByIdExercise(idExercise)
        assertTrue { setsInCache.containsAll(sets) }

        val setsInNetwork = exerciseSetNetworkDataSource.getExerciseSetByIdExercise(idExercise)
        if(!setsInNetwork.isNullOrEmpty()){
            assertTrue { setsInNetwork.containsAll(sets) }
        }
    }


    @Test
    fun insertMultipleExerciseSet_errors_confirmNetworkAndCacheUpdated() = runBlocking {
        val idExercise = "idExercise1"
        val sets : ArrayList<ExerciseSet> = ArrayList()
        repeat(3){
            val newExerciseSet = exerciseSetFactory.createExerciseSet(
                idExerciseSet = UUID.randomUUID().toString(),
                reps = null,
                weight = null,
                time = null,
                restTime = null,
                created_at = null
            )
            sets.add(newExerciseSet)
        }

        val newExerciseInvalid = exerciseSetFactory.createExerciseSet(
            idExerciseSet = FORCE_NEW_EXERCISESET_EXCEPTION,
            reps = null,
            weight = null,
            time = null,
            restTime = null,
            created_at = null
        )

        sets.add(newExerciseInvalid)

        //Insert
        insertMultipleExerciseSet?.insertMultipleExerciseSet(
            sets,
            idExercise,
            InsertMultipleExerciseSetEvent(sets, idExercise)
        )?.collect { value ->

            Assertions.assertEquals(
                value?.message?.response?.message,
                ADD_EXERCISE_SETS_ERRORS
            )
        }

        //Confirm invalid not inserted
        val setNotInCache = exerciseSetCacheDataSource.getExerciseSetById(newExerciseInvalid.idExerciseSet,idExercise)
        assertTrue { setNotInCache == null }

        val setNotInNetwork = exerciseSetNetworkDataSource.getExerciseSetById(newExerciseInvalid.idExerciseSet,idExercise)
        assertTrue { setNotInNetwork == null }

        sets.remove(newExerciseInvalid)

        //Confirm other inserted
        val setsInCache = exerciseSetCacheDataSource.getExerciseSetByIdExercise(idExercise)
        assertTrue { setsInCache.containsAll(sets) }

        val setsInNetwork = exerciseSetNetworkDataSource.getExerciseSetByIdExercise(idExercise)
        if(!setsInNetwork.isNullOrEmpty()){
            assertTrue { setsInNetwork.containsAll(sets) }
        }
    }

}
