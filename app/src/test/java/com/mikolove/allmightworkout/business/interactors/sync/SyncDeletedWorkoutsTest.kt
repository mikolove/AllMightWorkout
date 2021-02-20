package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.allmightworkout.business.data.cache.abstraction.WorkoutCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Workout
import com.mikolove.allmightworkout.business.interactors.sync.SyncDeletedWorkouts
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. deleteNetworkWorkouts_confirmCacheSync()
    a) select some workouts for deleting from network
    b) delete from network
    c) perform sync
    d) confirm workouts from cache were deleted
 */

class SyncDeletedWorkoutsTest {

    //System in test
    private val syncDeletedWorkouts : SyncDeletedWorkouts

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        syncDeletedWorkouts = SyncDeletedWorkouts(
            workoutCacheDataSource,
            workoutNetworkDataSource
        )
    }


    @Test
    fun deleteNetworkWorkouts_confirmCacheSync() = runBlocking {

        //Generate delete
        val networkWorkout = workoutNetworkDataSource.getWorkouts()
        val workoutToDelete : ArrayList<Workout> = ArrayList()
        for( wk in networkWorkout) {
            workoutToDelete.add(wk)
            workoutNetworkDataSource.insertDeleteWorkout(wk)
            if(workoutToDelete.size>2){
                break
            }
        }

        //Sync
        syncDeletedWorkouts.syncDeletedWorkouts()

        //Confirm
        for(workout in workoutToDelete){
            val cachedWorkout = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            assertTrue { cachedWorkout == null}
        }
    }

}