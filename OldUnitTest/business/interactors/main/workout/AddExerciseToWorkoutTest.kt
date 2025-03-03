package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_NEW_ADD_EXERCISE_WORKOUT_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION
import com.mikolove.core.data.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.data.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.data.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseType
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.interactors.workout.AddExerciseToWorkout.Companion.INSERT_WORKOUT_EXERCISE_FAILED
import com.mikolove.core.interactors.workout.AddExerciseToWorkout.Companion.INSERT_WORKOUT_EXERCISE_SUCCESS
import com.mikolove.core.interactors.workout.AddExerciseToWorkout.Companion.INSERT_WORKOUT_EXERCISE_UPDATE_FAILED
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. addExerciseToWorkout_success_confirmNetworkAndCacheUpdated()
    - Checking before updating is done somewhere else or maybe it will be not deal
    since DB should return an error and we don't care if it fails
    a) add a new Exercise into a workout
    b) listen for INSERT_WORKOUT_EXERCISE_SUCCESS emission from flow
    c) confirm cache was updated
    d) confirm network was updated
2. addExerciseToWorkout_fail_updateExerciseIdUpdatedDate_confirmNetworkAndCacheUnchanged
    a) add a new Exercise into a workout
    b) fail since exerciseIdsUpdatedAt fail to be updated
    c) listen for INSERT_WORKOUT_EXERCISE_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated

2. addExerciseToWorkout_fail_confirmNetworkAndCacheUnchanged()
    a) add a new Exercise into a workout
    b) force a failure (return -1 from db operation)
    c) listen for INSERT_WORKOUT_EXERCISE_FAILED emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) add a new Exercise into a workout
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    e) confirm cache was not updated
    e) confirm network was not updated
 */

@InternalCoroutinesApi
class AddExerciseToWorkoutTest {

    //System in test
    private val addExerciseToWorkout : com.mikolove.core.interactors.workout.AddExerciseToWorkout

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource
    private val exerciseFactory : ExerciseFactory
    private val bodyPartCacheDataState : BodyPartCacheDataSource
    private val dateUtil : DateUtil

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        dateUtil = dependencyContainer.dateUtil
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        exerciseFactory = dependencyContainer.exerciseFactory
        bodyPartCacheDataState = dependencyContainer.bodyPartCacheDataSource
        addExerciseToWorkout = com.mikolove.core.interactors.workout.AddExerciseToWorkout(
            workoutCacheDataSource,
            workoutNetworkDataSource,
            exerciseCacheDataSource,
            exerciseNetworkDataSource,
            dateUtil
        )
    }

    @Test
    fun addExerciseToWorkout_success_confirmNetworkAndCacheUpdated() = runBlocking {

        //Get an exercise
        val exerciseToAdd = exerciseCacheDataSource.getExerciseById("idExercise4")!!

        //Get a workout
        val workoutToFill = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Add exercise to workout
        addExerciseToWorkout.execute(
            idExercise = exerciseToAdd?.idExercise,
            idWorkout =  workoutToFill?.idWorkout,
            stateEvent = AddExerciseToWorkoutEvent(
                exerciseId = exerciseToAdd.idExercise,
                workoutId = workoutToFill.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_WORKOUT_EXERCISE_SUCCESS
                )
            }

        })

        //Confirm cache updated
        val isInCache = exerciseCacheDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInCache == 1 }

        //Confirm network updated
        val isInNetwork = exerciseNetworkDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInNetwork == 1 }

        val isCacheDateUpdated = workoutCacheDataSource.getExerciseIdsUpdate(workoutToFill.idWorkout)
        assertTrue { isCacheDateUpdated != ""}

        val isNetworkDateUpdated = workoutNetworkDataSource.getExerciseIdsUpdate(workoutToFill.idWorkout)
        assertTrue { isNetworkDateUpdated != ""}

    }


    @Test
    fun addExerciseToWorkout_fail_updateExerciseIdUpdatedDate_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Get an exercise
        val exerciseToAdd = exerciseCacheDataSource.getExerciseById("idExercise4")!!

        //Get a workout
        val workoutToFill = workoutCacheDataSource.getWorkoutById("idWorkout1")!!
        workoutToFill.idWorkout = FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION

        //Add exercise to workout
        addExerciseToWorkout.execute(
            idExercise = exerciseToAdd?.idExercise,
            idWorkout =  workoutToFill?.idWorkout,
            stateEvent = AddExerciseToWorkoutEvent(
                exerciseId = exerciseToAdd.idExercise,
                workoutId = workoutToFill.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_WORKOUT_EXERCISE_UPDATE_FAILED
                )
            }

        })

        //Confirm cache updated
        val isInCache = exerciseCacheDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInCache == 0 }

        //Confirm network updated
        val isInNetwork = exerciseNetworkDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInNetwork == 0 }

        val isCacheDateUpdated = workoutCacheDataSource.getExerciseIdsUpdate(workoutToFill.idWorkout)
        assertTrue { isCacheDateUpdated == ""}

        val isNetworkDateUpdated = workoutNetworkDataSource.getExerciseIdsUpdate(workoutToFill.idWorkout)
        assertTrue { isNetworkDateUpdated == ""}

    }

    @Test
    fun addExerciseToWorkout_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Create invalid exercise
        val bodyPartToAdd = bodyPartCacheDataState.getBodyPartById("idBodyPart1")
        val exerciseToAdd = exerciseFactory.createExercise(
            idExercise = FORCE_GENERAL_FAILURE,
            name = "new exercise",
            sets = null,
            bodyPart = bodyPartToAdd!!,
            exerciseType = ExerciseType.REP_EXERCISE,
            created_at = null
        )

        //Get a workout
        val workoutToFill = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Add exercise to workout
        addExerciseToWorkout.execute(
            idExercise = exerciseToAdd?.idExercise,
            idWorkout =  workoutToFill?.idWorkout,
            stateEvent = AddExerciseToWorkoutEvent(
                exerciseId = exerciseToAdd.idExercise,
                workoutId = workoutToFill.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    INSERT_WORKOUT_EXERCISE_FAILED
                )
            }

        })

        //Confirm cache updated
        val isInCache = exerciseCacheDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInCache == 0 }

        //Confirm network updated
        val isInNetwork = exerciseNetworkDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInNetwork == 0 }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Create invalid exercise
        val bodyPartToAdd = bodyPartCacheDataState.getBodyPartById("idBodyPart1")
        val exerciseToAdd = exerciseFactory.createExercise(
            idExercise = FORCE_NEW_ADD_EXERCISE_WORKOUT_EXCEPTION,
            name = "new exercise",
            sets = null,
            bodyPart = bodyPartToAdd!!,
            exerciseType = ExerciseType.REP_EXERCISE,
            created_at = null
        )

        //Get a workout
        val workoutToFill = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Add exercise to workout
        addExerciseToWorkout.execute(
            idExercise = exerciseToAdd?.idExercise,
            idWorkout =  workoutToFill?.idWorkout,
            stateEvent = AddExerciseToWorkoutEvent(
                exerciseId = exerciseToAdd.idExercise,
                workoutId = workoutToFill.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }

        })

        //Confirm cache updated
        val isInCache = exerciseCacheDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInCache == 0 }

        //Confirm network updated
        val isInNetwork = exerciseNetworkDataSource.isExerciseInWorkout(workoutToFill.idWorkout,exerciseToAdd.idExercise)
        assertTrue {  isInNetwork == 0 }

    }

}