package com.mikolove.exercise.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mikolove.core.data.workers.toWorkerResult
import com.mikolove.core.domain.util.Result.Error
import com.mikolove.core.domain.util.Result.Success

class FetchExerciseWorker (
    context : Context,
    params : WorkerParameters,
    private val offlineFirstExerciseRepository: OfflineFirstExerciseRepository
) : CoroutineWorker(context,params){

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = offlineFirstExerciseRepository.fetchExercises()) {
            is Error -> {
                result.error.toWorkerResult()
            }

            is Success -> Result.success()
        }
    }
}