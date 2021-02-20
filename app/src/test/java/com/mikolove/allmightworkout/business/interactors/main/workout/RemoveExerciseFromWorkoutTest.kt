package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_GENERAL_FAILURE
import com.mikolove.allmightworkout.business.data.cache.FORCE_REMOVE_EXERCISE_WORKOUT_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.BodyPartCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.ExerciseFactory
import com.mikolove.allmightworkout.business.domain.model.ExerciseType
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_FAILED
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_SUCCESS
import com.mikolove.allmightworkout.business.interactors.main.workout.RemoveExerciseFromWorkout.Companion.REMOVE_WORKOUT_EXERCISE_UPDATE_FAILED
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout.state.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. removeExerciseFromWorkout_success_confirmNetworkUpdated()
    a) delete an exercise from workout
    b) check for success message from flow emission
    c) confirm exercise was deleted from network
    d) confirm exercise was deleted from cache
2. removeExerciseFromWorkout_fail_updateExerciseIdUpdatedDate_confirmNetworkUnchanged
    a) attempt to delete a exercise from workout, fail since exerciseIdsUpdatedAt fail to be updated
    b) check for failure message from flow emission
    c) confirm network was not changed
3. removeExerciseFromWorkout_fail_confirmNetworkUnchanged()
    a) attempt to delete a exercise from workout, fail since does not exist
    b) check for failure message from flow emission
    c) confirm network was not changed
3. throwException_checkGenericError_confirmNetworkUnchanged()
    a) attempt to delete a exercise from workout, force an exception to throw
    b) check for failure message from flow emission
    c) confirm network was not changed
 */


@InternalCoroutinesApi
class RemoveExerciseFromWorkoutTest {

    //System in test
    private val removeExerciseFromWorkout : RemoveExerciseFromWorkout

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val dateUtil : DateUtil
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource
    private val exerciseFactory : ExerciseFactory
    private val bodyPartCacheDataState : BodyPartCacheDataSource

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
        removeExerciseFromWorkout = RemoveExerciseFromWorkout(
            workoutCacheDataSource,
            workoutNetworkDataSource,
            exerciseCacheDataSource,
            exerciseNetworkDataSource,
            dateUtil
        )
    }


    @Test
    fun removeExerciseFromWorkout_success_confirmNetworkUpdated() = runBlocking {

        //Get a workout
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Select exercise
        val exerciseToRemove = workout.exercises?.get(0)!!

        //Remove it
        removeExerciseFromWorkout.removeExerciseFromWorkout(
            idExercise = exerciseToRemove.idExercise,
            idWorkout = workout.idWorkout,
            stateEvent = RemoveExerciseFromWorkoutEvent(
                exerciseId = exerciseToRemove.idExercise,
                workoutId = workout.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    REMOVE_WORKOUT_EXERCISE_SUCCESS
                )
            }
        })

        //Confirm cache updated
        val cacheUpdated = exerciseCacheDataSource.isExerciseInWorkout(workout.idWorkout,exerciseToRemove.idExercise)
        assertTrue{ cacheUpdated == 0}

        //Confirm network updated
        val networkUpdated = exerciseNetworkDataSource.isExerciseInWorkout(workout.idWorkout,exerciseToRemove.idExercise)
        assertTrue{ networkUpdated == 0}

        val isCacheDateUpdated = workoutCacheDataSource.getExerciseIdsUpdate(workout.idWorkout)
        assertTrue { isCacheDateUpdated != ""}

        val isNetworkDateUpdated = workoutNetworkDataSource.getExerciseIdsUpdate(workout.idWorkout)
        assertTrue { isNetworkDateUpdated != ""}
    }

    @Test
    fun removeExerciseFromWorkout_fail_updateExerciseIdUpdatedDate_confirmNetworkUnchanged() = runBlocking {

        //Get a workout
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!
        workout.idWorkout = FORCE_UPDATE_WORKOUT_EXERCISE_IDS_EXCEPTION

        //Select exercise
        val exerciseToRemove = workout.exercises?.get(0)!!

        val beforeCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)

        //Remove it
        removeExerciseFromWorkout.removeExerciseFromWorkout(
            idExercise = exerciseToRemove.idExercise,
            idWorkout = workout.idWorkout,
            stateEvent = RemoveExerciseFromWorkoutEvent(
                exerciseId = exerciseToRemove.idExercise,
                workoutId = workout.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    REMOVE_WORKOUT_EXERCISE_UPDATE_FAILED
                )
            }
        })

        //Cache before
        val afterCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)
        val afterNetworkRemove = exerciseNetworkDataSource.getExercisesByWorkout(workout.idWorkout)


        //Check workout unchanged from cache
        assertTrue { beforeCacheRemove == afterCacheRemove}

        //Check workout unchanged from network
        assertTrue { afterCacheRemove == afterNetworkRemove}

        val isCacheDateUpdated = workoutCacheDataSource.getExerciseIdsUpdate(workout.idWorkout)
        assertTrue { isCacheDateUpdated == ""}

        val isNetworkDateUpdated = workoutNetworkDataSource.getExerciseIdsUpdate(workout.idWorkout)
        assertTrue { isNetworkDateUpdated == ""}

    }

    @Test
    fun removeExerciseFromWorkout_fail_confirmNetworkUnchanged() = runBlocking {

        //Get a workout
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Create invalid exercise
        val bodyPart = bodyPartCacheDataState.getBodyPartById("idBodyPart1")
        val exercise = exerciseFactory.createExercise(
            idExercise = FORCE_GENERAL_FAILURE,
            name = "new exercise",
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = ExerciseType.REP_EXERCISE,
            created_at = null
        )

        //Cache before
        val beforeCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)

        //Remove it
        removeExerciseFromWorkout.removeExerciseFromWorkout(
            idExercise = exercise.idExercise,
            idWorkout = workout.idWorkout,
            stateEvent = RemoveExerciseFromWorkoutEvent(
                exerciseId = exercise.idExercise,
                workoutId = workout.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    REMOVE_WORKOUT_EXERCISE_FAILED
                )
            }
        })

        //Cache before
        val afterCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)
        val afterNetworkRemove = exerciseNetworkDataSource.getExercisesByWorkout(workout.idWorkout)


        //Check workout unchanged from cache
        assertTrue { beforeCacheRemove == afterCacheRemove}

        //Check workout unchanged from network
        assertTrue { afterCacheRemove == afterNetworkRemove}
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking {

        //Get a workout
        val workout = workoutCacheDataSource.getWorkoutById("idWorkout1")!!

        //Create invalid exercise
        val bodyPart = bodyPartCacheDataState.getBodyPartById("idBodyPart1")
        val exercise = exerciseFactory.createExercise(
            idExercise = FORCE_REMOVE_EXERCISE_WORKOUT_EXCEPTION,
            name = "new exercise",
            sets = null,
            bodyPart = bodyPart!!,
            exerciseType = ExerciseType.REP_EXERCISE,
            created_at = null
        )

        //Cache before
        val beforeCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)

        //Remove it
        removeExerciseFromWorkout.removeExerciseFromWorkout(
            idExercise = exercise.idExercise,
            idWorkout = workout.idWorkout,
            stateEvent = RemoveExerciseFromWorkoutEvent(
                exerciseId = exercise.idExercise,
                workoutId = workout.idWorkout
            )
        ).collect( object : FlowCollector<DataState<WorkoutViewState>?> {
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Cache before
        val afterCacheRemove = exerciseCacheDataSource.getExercisesByWorkout(workout.idWorkout)
        val afterNetworkRemove = exerciseNetworkDataSource.getExercisesByWorkout(workout.idWorkout)


        //Check workout unchanged from cache
        assertTrue { beforeCacheRemove == afterCacheRemove}

        //Check workout unchanged from network
        assertTrue { afterCacheRemove == afterNetworkRemove}
    }
}