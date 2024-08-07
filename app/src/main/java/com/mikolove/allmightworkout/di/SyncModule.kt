package com.mikolove.allmightworkout.di

import android.net.ConnectivityManager
import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.data.network.abstraction.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSetCacheDataSource
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

     @Singleton
     @Provides
     fun provideSyncNetworkConnectivity(
        connectivity: ConnectivityManager
     ) : SyncNetworkConnectivity {
         return SyncNetworkConnectivity(connectivity)
     }

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
        @SimpleDateFormatUS dateFormatUs: SimpleDateFormat,
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    ) : SyncExercises {
        return SyncExercises(
            dateFormat = dateFormatUs,
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource,
            exerciseSetCacheDataSource = exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource = exerciseSetNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncWorkouts(
        @SimpleDateFormatUS dateFormatUs: SimpleDateFormat,
        workoutCacheDataSource : WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource,
    ) : SyncWorkouts {
        return SyncWorkouts(
            dateFormat = dateFormatUs,
            workoutCacheDataSource = workoutCacheDataSource,
            workoutNetworkDataSource = workoutNetworkDataSource
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
}