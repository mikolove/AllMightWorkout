package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.data.network.abstraction.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.allmightworkout.framework.presentation.main.loading.NetworkSyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {


    @Singleton
    @Provides
    fun provideSyncWorkoutTypesAndBodyPart(
        bodyPartCacheDataSource: BodyPartCacheDataSource,
        workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
        workoutTypeNetworkDataSource: WorkoutTypeNetworkDataSource
    ) : SyncWorkoutTypesAndBodyPart {
        return SyncWorkoutTypesAndBodyPart(
            bodyPartCacheDataSource = bodyPartCacheDataSource,
            workoutTypeCacheDataSource = workoutTypeCacheDataSource,
            workoutTypeNetworkDataSource = workoutTypeNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncDeletedExercises(
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource
    ) : SyncDeletedExercises {
        return SyncDeletedExercises(
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncDeletedExercisesSets(
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    ) : SyncDeletedExerciseSets {
        return SyncDeletedExerciseSets(
            exerciseSetCacheDataSource = exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource = exerciseSetNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncDeletedWorkouts(
        workoutCacheDataSource : WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource
    ) : SyncDeletedWorkouts {
        return SyncDeletedWorkouts(
            workoutCacheDataSource = workoutCacheDataSource,
            workoutNetworkDataSource = workoutNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncExercises(
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    ) : SyncExercises {
        return SyncExercises(
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource,
            exerciseSetCacheDataSource = exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource = exerciseSetNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncWorkouts(
        workoutCacheDataSource : WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource,
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource
    ) : SyncWorkouts {
        return SyncWorkouts(
            workoutCacheDataSource = workoutCacheDataSource,
            workoutNetworkDataSource = workoutNetworkDataSource,
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncWorkoutExercises(
        workoutCacheDataSource : WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource,
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        dateUtil: DateUtil
    ) : SyncWorkoutExercises {
        return SyncWorkoutExercises(
            workoutCacheDataSource = workoutCacheDataSource,
            workoutNetworkDataSource = workoutNetworkDataSource,
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource,
            dateUtil = dateUtil
        )
    }

    @Singleton
    @Provides
    fun provideSyncHistory(
        historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
        historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
        historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
        historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource
    ) : SyncHistory {
        return SyncHistory(
            historyWorkoutCacheDataSource = historyWorkoutCacheDataSource,
            historyExerciseCacheDataSource = historyExerciseCacheDataSource,
            historyExerciseSetCacheDataSource = historyExerciseSetCacheDataSource,
            historyWorkoutNetworkDataSource = historyWorkoutNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideNetworkSyncManager(
        syncWorkoutTypesAndBodyPart: SyncWorkoutTypesAndBodyPart,
        syncDeletedExerciseSets: SyncDeletedExerciseSets,
        syncDeletedExercises: SyncDeletedExercises,
        syncDeletedWorkouts: SyncDeletedWorkouts,
        syncHistory: SyncHistory,
        syncExercises: SyncExercises,
        syncWorkouts: SyncWorkouts,
        syncWorkoutExercises: SyncWorkoutExercises
    ) : NetworkSyncManager {
        return NetworkSyncManager(
            syncWorkoutTypesAndBodyPart = syncWorkoutTypesAndBodyPart,
            syncDeletedExerciseSets = syncDeletedExerciseSets,
            syncDeletedExercises = syncDeletedExercises,
            syncDeletedWorkouts = syncDeletedWorkouts,
            syncHistory = syncHistory,
            syncExercises = syncExercises,
            syncWorkouts = syncWorkouts,
            syncWorkoutExercises = syncWorkoutExercises
        )
    }
}