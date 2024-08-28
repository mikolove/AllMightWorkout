package com.mikolove.allmightworkout.business.interactors.sync

import com.mikolove.core.data.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.data.exercise.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSet
import com.mikolove.allmightworkout.di.DependencyContainer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


/*
Test cases:
1. deleteNetworkExerciseSets_confirmCacheSync()
    a) select some exercise sets for deleting from network
    b) delete from network
    c) perform sync
    d) confirm exercise sets from cache were deleted
 */

//TODO : No test needed firestore changed
class SyncDeletedExerciseSetsTest {

    //System in test
    private val syncDeletedExerciseSets : com.mikolove.core.interactors.sync.SyncDeletedExerciseSets

    //Dependencies
    private val dependencyContainer : DependencyContainer
    private val exerciseSetCacheDataSource : ExerciseSetCacheDataSource
    private val exerciseSetNetworkDataSource : ExerciseSetNetworkDataSource

    init {
        dependencyContainer = DependencyContainer()
        dependencyContainer.build()
        exerciseSetCacheDataSource = dependencyContainer.exerciseSetCacheDataSource
        exerciseSetNetworkDataSource = dependencyContainer.exerciseSetNetworkDataSource
        syncDeletedExerciseSets = com.mikolove.core.interactors.sync.SyncDeletedExerciseSets(
            exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource
        )
    }


    @Test
    fun deleteNetworkExerciseSets_confirmCacheSync() = runBlocking {

        //Generate delete
        val networkExerciseSets1 = exerciseSetNetworkDataSource.getExerciseSetByIdExercise("idExercise1")!!
        val networkExerciseSets2 = exerciseSetNetworkDataSource.getExerciseSetByIdExercise("idExercise2")!!

        val exerciseSetToDelete : HashMap<String, ExerciseSet> = HashMap()
        for( eS in networkExerciseSets1) {
            exerciseSetToDelete.put("idExercise1",eS)
            exerciseSetNetworkDataSource.insertDeletedExerciseSet(eS)
            if(exerciseSetToDelete.size>1){
                break
            }
        }
        for( eS in networkExerciseSets2) {
            exerciseSetToDelete.put("idExercise2",eS)
            exerciseSetNetworkDataSource.insertDeletedExerciseSet(eS)
            if(exerciseSetToDelete.size>1){
                break
            }
        }

        //Sync
        syncDeletedExerciseSets.syncDeletedExerciseSets()

        //Confirm
        for((key, exerciseSet) in exerciseSetToDelete){
            val cachedExerciseSet = exerciseSetCacheDataSource.getExerciseSetById(exerciseSet.idExerciseSet,key)
            Assertions.assertTrue { cachedExerciseSet == null}
        }
    }

}