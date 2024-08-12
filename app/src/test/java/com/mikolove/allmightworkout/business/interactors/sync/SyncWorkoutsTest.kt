package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseNetworkDataSource
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*
Test cases:
In all those case check Workout
1. insertCachedWorkoutsIntoNetwork()
    a) insert a bunch of new workout into the cache
    b) perform the sync
    c) check to see that those workouts were inserted into the network
2. insertNetworkWorkoutsIntoCache()
    a) insert a bunch of new workouts into the network
    b) perform the sync
    c) check to see that those workouts were inserted into the cache
3. checkCacheUpdateLogicSync()
    a) select some workouts from the cache and update them
    b) perform sync
    c) confirm network reflects the updates
4. checkNetworkUpdateLogicSync()
    a) select some workouts from the network and update them
    b) perform sync
    c) confirm cache reflects the updates
 */

class SyncWorkoutsTest {

    //System in test
    private val syncWorkouts : SyncWorkouts

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val workoutFactory : WorkoutFactory

    private val dateFormat : SimpleDateFormat

    private val workoutCacheDataSource : WorkoutCacheDataSource
    private val workoutNetworkDataSource : WorkoutNetworkDataSource
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource

    private val newWorkouts : ArrayList<Workout> = ArrayList()

    init {

        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        dateFormat = dependencyContainer.dateFormat
        workoutFactory = dependencyContainer.workoutFactory
        workoutCacheDataSource = dependencyContainer.workoutCacheDataSource
        workoutNetworkDataSource = dependencyContainer.workoutNetworkDataSource
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        syncWorkouts = SyncWorkouts(
            dateFormat,
            workoutCacheDataSource,
            workoutNetworkDataSource,
            exerciseCacheDataSource,
            exerciseNetworkDataSource
        )

        (1..3).forEach {
            val workout = workoutFactory.createWorkout(
                idWorkout = UUID.randomUUID().toString(),
                name = null,
                exercises = null,
                isActive = null,
                created_at = null
            )
            newWorkouts.add(workout)
        }

    }

    @Test
    fun insertCachedWorkoutsIntoNetwork() = runBlocking {

        //Insert new workout in cache
        for(workout in newWorkouts){
            workoutCacheDataSource.insertWorkout(workout)
        }

        //Sync
        syncWorkouts.syncWorkouts()

        //Check if workout are in network
        for (workout in newWorkouts){
            val networkWorkout = workoutNetworkDataSource.getWorkoutById(workout.idWorkout)
            assertTrue { networkWorkout != null}
        }
    }

    @Test
    fun insertNetworkWorkoutsIntoCache() = runBlocking {

        //Insert new workout in network
        for(workout in newWorkouts){
            workoutNetworkDataSource.insertWorkout(workout)
        }

        //Sync
        syncWorkouts.syncWorkouts()

        //Check if workout are in network
        for (workout in newWorkouts){
            val cacheWorkout = workoutCacheDataSource.getWorkoutById(workout.idWorkout)
            assertTrue { cacheWorkout != null}
        }
    }

    @Test
    fun checkCacheUpdateLogicSync() = runBlocking {

        //Update a workout in cache
        val cachedWorkout = workoutCacheDataSource.getWorkoutById("idWorkout1")

        val updatedWorkout = workoutFactory.createWorkout(
            idWorkout = cachedWorkout?.idWorkout,
            name = "new new",
            exercises = cachedWorkout?.exercises,
            isActive = false,
            created_at = cachedWorkout?.createdAt
        )

        workoutCacheDataSource.updateWorkout(
            updatedWorkout.idWorkout,
            updatedWorkout.name,
            updatedWorkout.isActive,
            updatedWorkout.updatedAt
        )

        //Sync
        syncWorkouts.syncWorkouts()

        //Check if network workout is updated
        val networkWorkout = workoutNetworkDataSource.getWorkoutById("idWorkout1")
        assertTrue { updatedWorkout.name == networkWorkout?.name}
        assertTrue { updatedWorkout.isActive == networkWorkout?.isActive}
        assertTrue { updatedWorkout.updatedAt == networkWorkout?.updatedAt }
    }

    @Test
    fun checkNetworkUpdateLogicSync() = runBlocking {
        //Update a workout in cache
        val networkWorkout = workoutNetworkDataSource.getWorkoutById("idWorkout1")

        val updatedWorkout = workoutFactory.createWorkout(
            idWorkout = networkWorkout?.idWorkout,
            name = "new new new",
            exercises = networkWorkout?.exercises,
            isActive = false,
            created_at = networkWorkout?.createdAt
        )

        workoutNetworkDataSource.updateWorkout(
            updatedWorkout
        )

        //Sync
        syncWorkouts.syncWorkouts()

        //Check if network workout is updated
        val cacheWorkout = workoutCacheDataSource.getWorkoutById("idWorkout1")
        assertTrue { updatedWorkout.name == cacheWorkout?.name}
        assertTrue { updatedWorkout.isActive == cacheWorkout?.isActive}
        assertTrue { updatedWorkout.updatedAt == cacheWorkout?.updatedAt }

    }

}