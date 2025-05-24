package com.mikolove.core.data.repositories

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.loading.LoadingError
import com.mikolove.core.domain.loading.LoadingRepository
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.GroupNetworkDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService

class LoadingRepositoryImpl(
    private val workoutTypeNetworkService: WorkoutTypeNetworkService,
    private val workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
    private val groupCacheDataSource: GroupCacheDataSource,
    private val groupNetworkDataSource: GroupNetworkDataSource,
    private val bodyPartCacheDataSource: BodyPartCacheDataSource,
    private val sessionStorage : SessionStorage,
)  : LoadingRepository{

    override suspend fun loadGroups(): EmptyResult<DataError> {
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        val networkResult = safeApiCall { groupNetworkDataSource.getGroups() }

        if(networkResult !is Result.Success){
            return networkResult.asEmptyDataResult()
        } else{

            val groups = networkResult.data

            if(groups.isNotEmpty()){
                val cacheResult = safeCacheCall {
                    groupCacheDataSource.upsertGroups(groups, userId)
                }
                return when(cacheResult){
                    is Result.Error ->{
                        cacheResult.asEmptyDataResult()
                    }
                    is Result.Success ->{
                        Result.Success(Unit)
                    }
                }
            }else{
                return Result.Success(Unit)
            }
        }
    }

    override suspend fun loadWorkoutTypes(): EmptyResult<DataError> {

        val networkResult = safeApiCall {
            workoutTypeNetworkService.getWorkoutTypes()
        }

        if(networkResult !is Result.Success){
            return networkResult.asEmptyDataResult()
        } else{

            val workoutTypes = networkResult.data

            if(workoutTypes.isEmpty()){
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
