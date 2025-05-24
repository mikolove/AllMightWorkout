package com.mikolove.core.interactors.workout

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Error
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource

/*class
UpsertWorkout(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
)  {

    suspend fun execute(
        workout :Workout ,
        idUser : String
    ) : EmptyResult<UpsertError>{

        val cacheResult = safeCacheCall{
            workoutCacheDataSource.upsertWorkout(workout,idUser)
        }
        if(cacheResult !is Result.Success){
            return Result.Error(UpsertError.UPSERT_FAILED)
        }

        val remoteResult = safeApiCall{
            workoutNetworkDataSource.upsertWorkout(workout)
        }


        return when(remoteResult){
            is Result.Error ->{
                //Schedule for later
                Result.Success(Unit)
            }
            is Result.Success -> {
                 remoteResult.asEmptyDataResult()
            }
        }
    }

    enum class UpsertError : Error{
        UPSERT_FAILED
    }
}*/