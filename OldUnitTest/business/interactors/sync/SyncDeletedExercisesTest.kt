package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.core.data.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.Exercise
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
    private val syncDeletedExercises : com.mikolove.core.interactors.sync.SyncDeletedExercises

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseCacheDataSource : ExerciseCacheDataSource
    private val exerciseNetworkDataSource : ExerciseNetworkDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseCacheDataSource = dependencyContainer.exerciseCacheDataSource
        exerciseNetworkDataSource = dependencyContainer.exerciseNetworkDataSource
        syncDeletedExercises = com.mikolove.core.interactors.sync.SyncDeletedExercises(
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