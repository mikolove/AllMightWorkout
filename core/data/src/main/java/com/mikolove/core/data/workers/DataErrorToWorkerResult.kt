package com.mikolove.core.data.workers

import androidx.work.ListenableWorker
import com.mikolove.core.domain.util.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when(this) {
        DataError.Local.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.Local.NO_USER_FOUND-> ListenableWorker.Result.failure()
        DataError.Local.EXECUTION_ERROR -> ListenableWorker.Result.retry()
        DataError.Local.UNKNOWN -> ListenableWorker.Result.failure()
        DataError.Network.REQUEST_TIMEOUT -> ListenableWorker.Result.retry()
        DataError.Network.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.Network.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.Network.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.Network.UNKNOWN -> ListenableWorker.Result.failure()
        else -> ListenableWorker.Result.failure()
    }
}