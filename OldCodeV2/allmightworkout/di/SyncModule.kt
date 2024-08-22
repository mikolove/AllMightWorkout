package com.mikolove.allmightworkout.di

import android.net.ConnectivityManager
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.allmightworkout.business.interactors.sync.*
import com.mikolove.core.domain.analytics.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutNetworkDataSource
import com.mikolove.core.domain.bodypart.BodyPartCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeNetworkDataSource
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
     ) : com.mikolove.core.interactors.sync.SyncNetworkConnectivity {
         return com.mikolove.core.interactors.sync.SyncNetworkConnectivity(connectivity)
     }

    @Singleton
    @Provides
    fun provideSyncWorkoutTypesAndBodyPart(
        bodyPartCacheDataSource: BodyPartCacheDataSource,
        workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
        workoutTypeNetworkDataSource: WorkoutTypeNetworkDataSource
    ) : com.mikolove.core.interactors.sync.SyncWorkoutTypesAndBodyPart {
        return com.mikolove.core.interactors.sync.SyncWorkoutTypesAndBodyPart(
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
    ) : com.mikolove.core.interactors.sync.SyncDeletedExercises {
        return com.mikolove.core.interactors.sync.SyncDeletedExercises(
            exerciseCacheDataSource = exerciseCacheDataSource,
            exerciseNetworkDataSource = exerciseNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncDeletedExercisesSets(
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource
    ) : com.mikolove.core.interactors.sync.SyncDeletedExerciseSets {
        return com.mikolove.core.interactors.sync.SyncDeletedExerciseSets(
            exerciseSetCacheDataSource = exerciseSetCacheDataSource,
            exerciseSetNetworkDataSource = exerciseSetNetworkDataSource
        )
    }

    @Singleton
    @Provides
    fun provideSyncDeletedWorkouts(
        workoutCacheDataSource : WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource
    ) : com.mikolove.core.interactors.sync.SyncDeletedWorkouts {
        return com.mikolove.core.interactors.sync.SyncDeletedWorkouts(
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
    ) : com.mikolove.core.interactors.sync.SyncExercises {
        return com.mikolove.core.interactors.sync.SyncExercises(
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
    ) : com.mikolove.core.interactors.sync.SyncWorkouts {
        return com.mikolove.core.interactors.sync.SyncWorkouts(
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
    ) : com.mikolove.core.interactors.sync.SyncWorkoutExercises {
        return com.mikolove.core.interactors.sync.SyncWorkoutExercises(
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
    ) : com.mikolove.core.interactors.sync.SyncHistory {
        return com.mikolove.core.interactors.sync.SyncHistory(
            historyWorkoutCacheDataSource = historyWorkoutCacheDataSource,
            historyExerciseCacheDataSource = historyExerciseCacheDataSource,
            historyExerciseSetCacheDataSource = historyExerciseSetCacheDataSource,
            historyWorkoutNetworkDataSource = historyWorkoutNetworkDataSource
        )
    }
}