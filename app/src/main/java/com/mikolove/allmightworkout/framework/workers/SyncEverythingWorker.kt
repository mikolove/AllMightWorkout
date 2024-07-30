package com.mikolove.allmightworkout.framework.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mikolove.allmightworkout.business.domain.state.DataState
import com.mikolove.allmightworkout.business.domain.state.GenericMessageInfo
import com.mikolove.allmightworkout.business.domain.state.MessageType
import com.mikolove.allmightworkout.business.domain.state.UIComponentType
import com.mikolove.allmightworkout.business.interactors.sync.SyncEverything
import com.mikolove.allmightworkout.business.interactors.sync.SyncInteractors
import com.mikolove.allmightworkout.business.interactors.sync.SyncState
import com.mikolove.allmightworkout.util.printLogD
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class SyncEverythingWorker @AssistedInject constructor(
    private val syncInteractors: SyncInteractors,
    @Assisted appContext : Context,
    @Assisted workerParams : WorkerParameters) :
    CoroutineWorker(appContext,workerParams){

    override suspend fun doWork(): Result {

        inputData.getString("USER_ID")?.let {idUser ->

            printLogD("SyncEverythingWorker", "Launch sync")

            var errorOccured : Boolean = false

            val listOfSync = sortedMapOf(
                0 to "workoutandbp",
                1 to "deletedsets",
                2 to "deletedexercises",
                3 to "deletedworkouts",
                4 to "history",
                5 to "exercises",
                6 to "workoutgroups",
                7 to "workouts",
                8 to "workoutexercises"
            )

            for (  sync in listOfSync){

                printLogD("SyncEverythingWorker", "Sync ${sync.value}")

                val dataState : DataState<SyncState> = when(sync.value){
                    "workoutandbp" ->  syncInteractors.syncWorkoutTypesAndBodyPart.execute()
                    "deletedsets" ->   syncInteractors.syncDeletedExerciseSets.execute()
                    "deletedexercises" -> syncInteractors.syncDeletedExercises.execute()
                    "deletedworkouts" -> syncInteractors.syncDeletedWorkouts.execute()
                    "history" -> syncInteractors.syncHistory.execute(idUser = idUser)
                    "exercises" -> syncInteractors.syncExercises.execute(idUser = idUser)
                    "workoutgroups" -> syncInteractors.syncWorkoutGroups.execute()
                    "workouts" -> syncInteractors.syncWorkouts.execute(idUser = idUser)
                    "workoutexercises" -> syncInteractors.syncWorkoutExercises.execute(idUser = idUser)
                    else -> {
                        DataState.data<SyncState>(
                            message = GenericMessageInfo.Builder()
                                .id("SyncEverything.Error")
                                .title(SyncEverything.SYNC_EV_ERROR_TITLE)
                                .description(SyncEverything.SYNC_EV_ERROR_DESCRIPTION)
                                .messageType(MessageType.Error)
                                .uiComponentType(UIComponentType.None),
                            data = SyncState.FAILURE
                        )
                    }
                }

                if(dataState.data == SyncState.FAILURE){
                    errorOccured = true
                    break
                }
            }

            printLogD("SyncEverythingWorker", "Sync ended")

            if(!errorOccured){
                return Result.success()
            }else{
                return Result.failure()
            }

        } ?: return Result.failure()

    }
}