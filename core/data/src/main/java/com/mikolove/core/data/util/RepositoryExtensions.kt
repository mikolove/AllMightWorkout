package com.mikolove.core.data.util

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.DataError.Local.DISK_FULL
import com.mikolove.core.domain.util.DataError.Local.EXECUTION_ERROR
import com.mikolove.core.domain.util.DataError.Local.UNKNOWN
import com.mikolove.core.domain.util.Result
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import java.nio.channels.UnresolvedAddressException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

/*suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT){
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}
*/

/*suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {

                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}*/

suspend fun <T> safeCacheCall(
    cacheCall: suspend () -> T
): Result<T,DataError.Local> {
    return try {
        Result.Success(cacheCall.invoke())
    } catch (exception : Exception) {
        val stackTrace =exception.printStackTrace()
        Timber.tag(stackTrace.javaClass.name).d(exception)
        when (exception) {
            is SQLiteFullException ->{
                Result.Error(DISK_FULL)
            }
            is SQLiteException ->{
                Result.Error(EXECUTION_ERROR)
            }
            else ->{
                Result.Error(UNKNOWN)
            }
        }
    }
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Result<T, DataError.Network> {
    val response =  try {
        Result.Success(apiCall.invoke())
    }  catch(e: UnresolvedAddressException) {
        val stackTrace =e.printStackTrace()
        Timber.tag(stackTrace.javaClass.name).d(e)
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        val stackTrace =e.printStackTrace()
        Timber.tag(stackTrace.javaClass.name).d(e)
        return Result.Error(DataError.Network.UNKNOWN)
    }
    return response
}