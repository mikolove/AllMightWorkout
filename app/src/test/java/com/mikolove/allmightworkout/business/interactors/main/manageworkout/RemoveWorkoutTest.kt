package com.mikolove.allmightworkout.business.interactors.main.manageworkout

import com.mikolove.allmightworkout.business.data.cache.CacheErrors
import com.mikolove.allmightworkout.business.data.cache.FORCE_DELETE_WORKOUT_EXCEPTION
import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.WorkoutFactory
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.RemoveWorkout.Companion.DELETE_WORKOUT_FAILED
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.RemoveWorkout.Companion.DELETE_WORKOUT_SUCCESS
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutStateEvent.RemoveWorkoutEvent
import com.mikolove.allmightworkout.framework.presentation.main.manageworkout.state.ManageWorkoutViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*


/*
Test cases:
1. deleteWorkout_success_confirmNetworkUpdated()
    a) delete a workout
    b) check for success message from flow emission
    c) confirm workout was deleted from network
    d) confirm workout was deleted from cache
2. deleteWorkout_fail_confirmNetworkUnchanged()
    a) attempt to delete a workout, fail since does not exist
    b) check for failure message from flow emission
    c) confirm network was not changed
3. throwException_checkGenericError_confirmNetworkUnchanged()
    a) attempt to delete a workout, force an exception to throw
    b) check for failure message from flow emission
    c) confirm network was not changed
 */

@InternalCoroutinesApi
class RemoveWorkoutTest {

    private val removeWorkout: RemoveWorkout<ManageWorkoutViewState>

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
        removeWorkout = RemoveWorkout(
            workoutCacheDataSource,
            workoutNetworkDataSource)
    }

    @Test
    fun deleteWorkout_success_confirmNetworkUpdated() = runBlocking {

        //Get workout
        val removedWorkout = workoutCacheDataSource.getWorkouts("","",1).get(0)

        //Remove it
        removeWorkout.removeWorkout(
            workout = removedWorkout,
            stateEvent = RemoveWorkoutEvent(removedWorkout)
        ).collect(object :FlowCollector<DataState<ManageWorkoutViewState>?>{

            override suspend fun emit(value: DataState<ManageWorkoutViewState>?) {
                assertEquals(
                    value?.stateMessage?.response?.message,
                    DELETE_WORKOUT_SUCCESS
                )
            }

        })

        //Check workout remove from cache
        var cacheRemoveWorkout = workoutCacheDataSource.getWorkoutById(removedWorkout.idWorkout)
        assertTrue { cacheRemoveWorkout == null}

        //Check workout remove from network
        var networkRemoveWorkout = workoutNetworkDataSource.getWorkoutById(removedWorkout.idWorkout)
        assertTrue { networkRemoveWorkout == null}

    }


     @Test
     fun deleteWorkout_fail_confirmNetworkUnchanged() = runBlocking {

         //Invalid workout to delete dont exist so should failed
         val workoutToDelete = workoutFactory.createWorkout(
             idWorkout = UUID.randomUUID().toString(),
             name = UUID.randomUUID().toString(),
             exercises = null,
             created_at = null
         )

         //Before
         var beforeWorkoutsInCache = workoutCacheDataSource.getTotalWorkout()

         //Remove it
         removeWorkout.removeWorkout(
             workout = workoutToDelete,
             stateEvent = RemoveWorkoutEvent(workoutToDelete)
         ).collect(object :FlowCollector<DataState<ManageWorkoutViewState>?>{

             override suspend fun emit(value: DataState<ManageWorkoutViewState>?) {
                 assertEquals(
                     value?.stateMessage?.response?.message,
                     DELETE_WORKOUT_FAILED
                 )
             }

         })

         //After
         var afterWorkoutsInCache = workoutCacheDataSource.getTotalWorkout()
         var workoutsInNetwork = workoutNetworkDataSource.getWorkoutTotalNumber()

         //Check workout unchanged from cache
         assertTrue { beforeWorkoutsInCache == afterWorkoutsInCache}

         //Check workout unchanged from network
         assertTrue { afterWorkoutsInCache == workoutsInNetwork}

     }

    @Test
    fun throwException_checkGenericError_confirmNetworkUnchanged() = runBlocking {

        //Invalid workout to delete dont exist so should failed
        val workoutToDelete = workoutFactory.createWorkout(
            idWorkout = FORCE_DELETE_WORKOUT_EXCEPTION,
            name = UUID.randomUUID().toString(),
            exercises = null,
            created_at = null
        )

        //Before
        var beforeWorkoutsInCache = workoutCacheDataSource.getTotalWorkout()

        removeWorkout.removeWorkout(
            workout = workoutToDelete,
            stateEvent = RemoveWorkoutEvent(workoutToDelete)
        ).collect(object :FlowCollector<DataState<ManageWorkoutViewState>?>{

            override suspend fun emit(value: DataState<ManageWorkoutViewState>?) {
                assert(
                    value?.stateMessage?.response?.message
                        ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
                )
            }

        })

        //After
        var afterWorkoutsInCache = workoutCacheDataSource.getTotalWorkout()
        var workoutsInNetwork = workoutNetworkDataSource.getWorkoutTotalNumber()

        //Check workout unchanged from cache
        assertTrue { beforeWorkoutsInCache == afterWorkoutsInCache}

        //Check workout unchanged from network
        assertTrue { afterWorkoutsInCache == workoutsInNetwork}
    }
}