package com.mikolove.core.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.loading.LoadingError
import com.mikolove.core.domain.loading.LoadingRepository
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService
import timber.log.Timber

class LoadingRepositoryImpl(
    private val workoutTypeNetworkService: WorkoutTypeNetworkService,
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
    private val bodyPartCacheDataSource: BodyPartCacheDataSource
)  : LoadingRepository{
    override suspend fun loadWorkoutTypes(): EmptyResult<DataError> {

        val networkResult = safeApiCall {
            workoutTypeNetworkService.getWorkoutTypes()
        }
        Timber.d("networkResult: $networkResult")
        if(networkResult !is Result.Success){
            return networkResult.asEmptyDataResult()
        } else{

            val workoutTypes = networkResult.data

            if(workoutTypes.isNullOrEmpty()){
                return Result.Error(LoadingError.WORKOUT_TYPE_NOT_FOUND)
            }

            val bodyParts  = workoutTypes.map {
                it.idWorkoutType to it.bodyParts
            }

            val resultWkt = safeCacheCall {
                workoutTypeCacheDataSource.upsertWorkoutType(workoutTypes)
            }

            if(resultWkt !is Result.Success){
                return resultWkt.asEmptyDataResult()
            }

            bodyParts.forEach{ value ->
                val bpResult = safeCacheCall {
                    bodyPartCacheDataSource.upsertBodyPart(value.second,value.first)
                }
                if(bpResult !is Result.Success){
                    return resultWkt.asEmptyDataResult()
                }
            }

            return Result.Success(Unit)
        }
    }
}
