package com.mikolove.workout.data

import com.mikolove.core.data.util.safeApiCall
import com.mikolove.core.data.util.safeCacheCall
import com.mikolove.core.domain.auth.SessionStorage
import com.mikolove.core.domain.util.DataError
import com.mikolove.core.domain.util.EmptyResult
import com.mikolove.core.domain.util.Result
import com.mikolove.core.domain.util.asEmptyDataResult
import com.mikolove.core.domain.util.map
import com.mikolove.core.domain.workout.Workout
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkDataSource
import com.mikolove.workout.domain.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class OfflineFirstWorkoutRepository(
    private val workoutCacheDataSource: WorkoutCacheDataSource,
    private val workoutNetworkDataSource: WorkoutNetworkDataSource,
    private val sessionStorage : SessionStorage,
    private val applicationScope : CoroutineScope,
) : WorkoutRepository{

    override fun getWorkouts(searchQuery: String): Flow<List<Workout>> = flow {
        val userId = sessionStorage.get()?.userId ?: return@flow
        emitAll(workoutCacheDataSource.getWorkouts(userId,searchQuery))
    }

    override suspend fun getWorkoutById(workoutId: String): Result<Workout, DataError> {
        return safeCacheCall {
            workoutCacheDataSource.getWorkoutById(workoutId)
        }.map { it }
    }

    override fun getWorkout(workoutId: String): Flow<Workout> {
        return workoutCacheDataSource.getWorkout(workoutId)
    }

    override fun getWorkoutsWithExercises(workoutTypes: List<String>): Flow<List<Workout>> = flow{
        val userId = sessionStorage.get()?.userId ?: return@flow
        emitAll(workoutCacheDataSource.getWorkoutByWorkoutType(workoutTypes, userId))
    }

    override fun getWorkoutsWithExercisesWithGroups(
        workoutTypes: List<String>,
        groups: List<String>
    ): Flow<List<Workout>> = flow {

        Timber.d("WTFF")
        val userId = sessionStorage.get()?.userId ?: return@flow

        Timber.d("workoutTypes $workoutTypes")
        Timber.d("idGroup $groups")
        Timber.d("idUser $userId")

        emitAll(workoutCacheDataSource.getWorkoutByWorkoutTypeByGroup(
            workoutTypes,
            groups,
            userId))
    }

    override suspend fun fetchWorkouts(): EmptyResult<DataError> {
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        return when (val result = safeApiCall { workoutNetworkDataSource.getWorkouts() }) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    safeCacheCall {
                        workoutCacheDataSource.upsertWorkouts(result.data,userId)
                    }.asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertWorkout(workout: Workout): EmptyResult<DataError> {
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.NO_USER_FOUND)

        val workoutCacheResult = safeCacheCall {
            workoutCacheDataSource.upsertWorkout(workout,userId)
        }
        if(workoutCacheResult !is Result.Success){
            return workoutCacheResult.asEmptyDataResult()
        }

        workout.groups.forEach { group ->

            val groupCacheResult = safeCacheCall {
                workoutCacheDataSource.addWorkoutToGroup(workout.idWorkout, group.idGroup)
            }
            if(groupCacheResult !is Result.Success){
                return groupCacheResult.asEmptyDataResult()
            }
        }

        val networkResult = safeApiCall {
            workoutNetworkDataSource.upsertWorkout(workout)
        }

        return when(networkResult){
            is Result.Error -> Result.Success(Unit)
            is Result.Success -> Result.Success(Unit)
        }
    }

    override suspend fun deleteWorkout(workoutId: String) {
        val cacheResult = safeCacheCall {
            workoutCacheDataSource.removeWorkout(workoutId)
        }

        if(cacheResult !is Result.Success){
            return
        }

        applicationScope.async {
            safeApiCall {
                workoutNetworkDataSource.removeWorkout(workoutId)
            }
        }.await()
    }
}