package com.mikolove.core.data.workers

import com.mikolove.core.domain.exercise.Exercise
import kotlin.time.Duration

interface SyncExerciseScheduler{

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType{
        data class FetchExercise(val interval : Duration) : SyncType
        data class DeleteExercise(val exerciseId : String) : SyncType
        class CreateExercise(val exercise : Exercise) : SyncType
    }
}