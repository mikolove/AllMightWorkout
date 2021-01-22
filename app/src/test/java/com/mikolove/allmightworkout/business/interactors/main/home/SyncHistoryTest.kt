package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryExerciseSetCacheDataSource
import com.mikolove.allmightworkout.business.data.cache.abstraction.HistoryWorkoutCacheDataSource
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@InternalCoroutinesApi
class SyncHistoryTest {

    //Dependencies
    private val dependencyContainer : DependencyContainer

    //private val historyWorkoutNetworkDataSource : HistoryWorkoutNetworkDataSource
    private val historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource
    private val historyExerciseCacheDataSource : HistoryExerciseCacheDataSource
    private val historyExerciseSetCacheDataSource : HistoryExerciseSetCacheDataSource


    init{
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()

        historyWorkoutCacheDataSource = dependencyContainer.historyWorkoutCacheDataSource
        historyExerciseCacheDataSource = dependencyContainer.historyExerciseCacheDataSource
        historyExerciseSetCacheDataSource = dependencyContainer.historyExerciseSetCacheDataSource
        //historyWorkoutNetworkDataSource = dependencyContainer.historyWorkoutNetworkDataSource
    }


    @Test
    fun test() = runBlocking {

        val history = historyWorkoutCacheDataSource.getLastHistoryWorkout()

        println(history)
    }

}