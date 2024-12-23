package com.mikolove.allmightworkout.business.interactors.main.workout

import com.mikolove.core.domain.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_UPDATE_WORKOUT_EXCEPTION
import com.mikolove.core.data.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.data.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.domain.state.DataState
import com.mikolove.core.interactors.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_FAILED
import com.mikolove.core.interactors.workout.UpdateWorkout.Companion.UPDATE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutStateEvent.*
import com.mikolove.allmightworkout.framework.presentation.main.workout_list.WorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*


/*
Test cases:
1. updateWorkout_success_confirmNetworkAndCacheUpdated()
    a) select a random workout from the cache
    b) update that workout
    c) confirm UPDATE_WORKOUT_SUCCESS msg is emitted from flow
    d) confirm workout is updated in network
    e) confirm workout is updated in cache
2. updateWorkout_fail_confirmNetworkAndCacheUnchanged()
    a) attempt to update a workout, fail since does not exist
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) attempt to update a workout, force an exception to throw
    b) check for failure message from flow emission
    c) confirm nothing was updated in the cache
 */


@InternalCoroutinesApi
class UpdateWorkoutTest {

    private val updateWorkout : com.mikolove.core.interactors.workout.UpdateWorkout

    //Dependencies
    private val dependencyContainer: DependencyContainer
    private val workoutCacheDataSource: WorkoutCacheDataSource
    private val workoutNetworkDataSource: WorkoutNetworkDataSource

    private val workoutFactory: WorkoutFactory

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        workoutFactory = dependencyContainer.workoutFactory
        updateWorkout = com.mikolove.core.interactors.workout.UpdateWorkout(
            workoutCacheDataSource,
            workoutNetworkDataSource
        )
    }


    @Test
    fun updateWorkout_success_confirmNetworkAndCacheUpdated() = runBlocking {

        //Get random workout
        val randomWorkout = workoutCacheDataSource.getWorkouts("","",1).get(0)
        print(randomWorkout)
        //Change workout values
        val updatedWorkout = workoutFactory.createWorkout(
            idWorkout = randomWorkout.idWorkout,
            name = "new name",
            exercises = null,
            isActive = false,
            created_at = randomWorkout.createdAt
        )

        //Launch update process
        updateWorkout.execute(
            workout = updatedWorkout,
            stateEvent = UpdateWorkoutEvent()
        ).collect(object : FlowCollector<DataState<WorkoutViewState>?> {
            //Check response message emit from flow
            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    UPDATE_WORKOUT_SUCCESS
                )
            }
        })

        //Confirm cache updated
        val cacheWorkoutThatWasUpdated = workoutCacheDataSource.getWorkoutById(updatedWorkout.idWorkout)
        assertTrue { updatedWorkout == cacheWorkoutThatWasUpdated}

        //Confirm network updated
        val networkWorkoutThatWasUpdated = workoutNetworkDataSource.getWorkoutById(updatedWorkout.idWorkout)
        assertTrue { updatedWorkout == networkWorkoutThatWasUpdated}
    }

    @Test
    fun updateWorkout_fail_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Create new workout
        val newWorkout = workoutFactory.createWorkout(
            idWorkout = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString(),
            isActive = true,
            exercises = null,
            created_at = null
        )

        //Try to update it but it not exist
        updateWorkout.execute(
            workout = newWorkout,
            stateEvent = UpdateWorkoutEvent()
        ).collect( object :FlowCollector<DataState<WorkoutViewState>?>{

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assertEquals(
                    value?.message?.response?.message,
                    UPDATE_WORKOUT_FAILED
                )
            }
        })

        //Confirm cache not updated
        val cacheWorkoutUpdated = workoutCacheDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { cacheWorkoutUpdated == null }

        //Confirm network not updated
        val networkWorkoutUpdated = workoutNetworkDataSource.getWorkoutById(newWorkout.idWorkout)
        assertTrue { cacheWorkoutUpdated == null }
    }


    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        //Force an exception on note
        val updatedWorkout = workoutFactory.createWorkout(
            idWorkout = FORCE_UPDATE_WORKOUT_EXCEPTION,
            name = UUID.randomUUID().toString(),
            exercises = null,
            created_at = null
        )

        updateWorkout.execute(
            workout = updatedWorkout,
            stateEvent = UpdateWorkoutEvent()
        ).collect(object : FlowCollector<DataState<WorkoutViewState>?> {

            override suspend fun emit(value: DataState<WorkoutViewState>?) {
                assert(
                    value?.message?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }
        })

        //Confirm cache was not updated
        val cacheWorkoutThatWasInserted = workoutCacheDataSource.getWorkoutById(updatedWorkout.idWorkout)
        assertTrue { cacheWorkoutThatWasInserted == null }

        //Confirm network was not updated
        val networkWorkoutThatWasInserted = workoutNetworkDataSource.getWorkoutById(updatedWorkout.idWorkout)
        assertTrue { networkWorkoutThatWasInserted == null }
    }

}