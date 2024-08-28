package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.core.data.analytics.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.core.data.analytics.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.core.data.analytics.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.di.DependencyContainer
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryWorkout
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

/*
1. insertCachedHistoryWorkoutsIntoNetwork()
    a) insert a bunch of new history workouts into the cache
    b) perform the sync
    c) check to see that those were inserted into the network
2. insertNetworkHistoryWorkoutsIntoCache()
    a) insert a bunch of new history workouts into the network
    b) perform the sync
    c) check to see that those were inserted into the cache
 */
@InternalCoroutinesApi
class SyncHistoryTest {

    //System in test
    private val syncHistory : com.mikolove.core.interactors.sync.SyncHistory

    //Dependencies
    private val dependencyContainer : DependencyContainer

    private val historyWorkoutNetworkDataSource : HistoryWorkoutNetworkDataSource
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource
    private val historyExerciseCacheDataSource : HistoryExerciseCacheDataSource
    private val historyExerciseSetCacheDataSource : HistoryExerciseSetCacheDataSource

    private val historyWorkoutFactory : HistoryWorkoutFactory
    private val historyExerciseFactory : HistoryExerciseFactory
    private val historyExerciseSetFactory : HistoryExerciseSetFactory

    private val newHistoryWorkouts : ArrayList<HistoryWorkout> = ArrayList()

    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        historyExerciseCacheDataSource = dependencyContainer.historyExerciseCacheDataSource
        historyExerciseSetCacheDataSource = dependencyContainer.historyExerciseSetCacheDataSource
        historyWorkoutNetworkDataSource = dependencyContainer.historyWorkoutNetworkDataSource

        historyWorkoutFactory = dependencyContainer.historyWorkoutFactory
        historyExerciseFactory = dependencyContainer.historyExerciseFactory
        historyExerciseSetFactory = dependencyContainer.historyExerciseSetFactory


        syncHistory = com.mikolove.core.interactors.sync.SyncHistory(
            historyWorkoutCacheDataSource,
            historyExerciseCacheDataSource,
            historyExerciseSetCacheDataSource,
            historyWorkoutNetworkDataSource
        )

        //Create additional data
        (1..3).forEach {
            val historySets = listOf(
                historyExerciseSetFactory.createHistoryExerciseSet(
                    idHistoryExerciseSet = UUID.randomUUID().toString(),
                    reps = null,
                    weight = null,
                    time = null,
                    restTime = null,
                    started_at = null,
                    ended_at = null,
                    created_at = null))

            val newHistoryExercises = listOf(
                historyExerciseFactory.createHistoryExercise(
                    idHistoryExercise = UUID.randomUUID().toString(),
                    null,
                    bodyPart = null,
                    workoutType = null,
                    exerciseType = null,
                    historySets = historySets,
                    startedAt = null,
                    endedAt = null,
                    createdAt = null))

            val newHistoryWorkout = historyWorkoutFactory.createHistoryWorkout(
                idHistoryWorkout = UUID.randomUUID().toString(),
                name =null,
                historyExercises = newHistoryExercises,
                started_at = null,
                ended_at = null,
                created_at = null
            )
            newHistoryWorkouts.add(newHistoryWorkout)
        }
    }



    @Test
    fun insertCachedHistoryWorkoutsIntoNetwork() = runBlocking {

        assertTrue{ newHistoryWorkouts.size > 0}

        //Insert into cache
        for(hW in newHistoryWorkouts){
            historyWorkoutCacheDataSource.insertHistoryWorkout(hW)
        }

        //Sync
        syncHistory.syncHistory()

        //Check if network updated
        for(hw in newHistoryWorkouts){
            val networkHistory = historyWorkoutNetworkDataSource.getHistoryWorkoutById(hw.idHistoryWorkout)
            assertTrue{ networkHistory != null}
        }
    }

    @Test
    fun insertNetworkHistoryWorkoutsIntoCache() = runBlocking {

        assertTrue{ newHistoryWorkouts.size > 0}

        //Insert into network
        for(hW in newHistoryWorkouts){
            historyWorkoutNetworkDataSource.insertHistoryWorkout(hW)
        }

        //Sync
        syncHistory.syncHistory()

        //Check if cache updated
        for(hw in newHistoryWorkouts){
            val networkHistory = historyWorkoutCacheDataSource.getHistoryWorkoutById(hw.idHistoryWorkout)
            assertTrue{ networkHistory != null}
        }
    }
}