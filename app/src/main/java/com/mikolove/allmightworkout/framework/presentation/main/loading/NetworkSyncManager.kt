package com.mikolove.allmightworkout.framework.presentation.main.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.allmightworkout.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/*
    Sync is performed following specific order

    - Workout Type and bodyPart
    - Delete exercises sets
    - Delete exercises
    - Delete workout
    - Sync history
    - Sync exercises
    - Sync workout
    - Sync workoutExercise
 */

class NetworkSyncManager
constructor(
    private val syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
    private val syncDeletedExerciseSets: SyncDeletedExerciseSets,
    private val syncDeletedExercises: SyncDeletedExercises,
    private val syncDeletedWorkouts: SyncDeletedWorkouts,
    private val syncHistory: SyncHistory,
    private val syncExercises: SyncExercises,
    private val syncWorkouts: SyncWorkouts,
    private val syncWorkoutExercises: SyncWorkoutExercises
){

    private val _hasSyncBeenExecuted: MutableLiveData<Boolean> = MutableLiveData(false)

    val hasSyncBeenExecuted: LiveData<Boolean>
        get() = _hasSyncBeenExecuted

    fun executeDataSync(coroutineScope: CoroutineScope){
       /* if(_hasSyncBeenExecuted.value!!){
            return
        }

        val syncJob = coroutineScope.launch {

            launch {
                printLogD("NetworkSyncManager", "syncing workoutTypes and bodyPart")
                syncWorkoutTypesAndBodyPart.syncWorkoutTypesAndBodyPart()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing delete exercise sets")
                syncDeletedExerciseSets.syncDeletedExerciseSets()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing delete exercises")
                syncDeletedExercises.syncDeletedExercises()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing delete workouts")
                syncDeletedWorkouts.syncDeletedWorkouts()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing history")
                syncHistory.syncHistory()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing exercises")
                syncExercises.syncExercises()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing workouts")
                syncWorkouts.syncWorkouts()
            }.join()

            launch {
                printLogD("NetworkSyncManager", "syncing workout exercises")
                syncWorkoutExercises.syncWorkoutExercises()
            }

        }
        syncJob.invokeOnCompletion {
            CoroutineScope(Main).launch{
                _hasSyncBeenExecuted.value = true
            }
        }*/
    }

}