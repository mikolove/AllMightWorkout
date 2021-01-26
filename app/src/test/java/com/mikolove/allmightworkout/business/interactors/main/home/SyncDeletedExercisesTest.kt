package com.mikolove.allmightworkout.business.interactors.main.home

import com.mikolove.allmightworkout.business.data.cache.abstraction.ExerciseCacheDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.Exercise
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


/*
Test cases:
1. deleteNetworkExercises_confirmCacheSync()
    a) select some exercises for deleting from network
    b) delete from network
    c) perform sync
    d) confirm exercises from cache were deleted
 */


class SyncDeletedExercisesTest {

    //System in test
    private val syncDeletedExercises : SyncDeletedExercises

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        syncDeletedExercises = SyncDeletedExercises(
            exerciseCacheDataSource,
            exerciseNetworkDataSource
        )
    }


    @Test
    fun deleteNetworkExercises_confirmCacheSync() = runBlocking {

        //Generate delete
        val networkExercises = exerciseNetworkDataSource.getExercises()
        val exercisesToDelete : ArrayList<Exercise> = ArrayList()
        for( wk in networkExercises) {
            exercisesToDelete.add(wk)
            exerciseNetworkDataSource.insertDeletedExercise(wk)
            if(exercisesToDelete.size>2){
                break
            }
        }

        //Sync
       syncDeletedExercises.syncDeletedExercises()

        //Confirm
        for(exercise in exercisesToDelete){
            val cachedWExercise = exerciseCacheDataSource.getExerciseById(exercise.idExercise)
            Assertions.assertTrue { cachedWExercise == null}
        }
    }

}