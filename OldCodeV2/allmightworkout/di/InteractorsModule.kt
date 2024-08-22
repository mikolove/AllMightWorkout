package com.mikolove.allmightworkout.di

import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.core.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.data.network.abstraction.*
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.core.interactors.auth.GetAuthState
import com.mikolove.core.interactors.auth.SignOut
import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.core.interactors.analytics.GetHistoryWorkoutDetail
import com.mikolove.core.interactors.analytics.GetHistoryWorkouts
import com.mikolove.core.interactors.analytics.GetTotalHistoryWorkouts
import com.mikolove.core.interactors.analytics.HistoryListInteractors
import com.mikolove.core.interactors.loading.LoadUser
import com.mikolove.core.interactors.loading.LoadingInteractors
import com.mikolove.core.interactors.session.SessionInteractors
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.*
import com.mikolove.core.interactors.sync.SyncDeletedExerciseSets
import com.mikolove.core.interactors.sync.SyncDeletedExercises
import com.mikolove.core.interactors.sync.SyncDeletedWorkouts
import com.mikolove.core.interactors.sync.SyncExercises
import com.mikolove.core.interactors.sync.SyncHistory
import com.mikolove.core.interactors.sync.SyncInteractors
import com.mikolove.core.interactors.sync.SyncNetworkConnectivity
import com.mikolove.core.interactors.sync.SyncWorkoutExercises
import com.mikolove.core.interactors.sync.SyncWorkoutGroups
import com.mikolove.core.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.core.interactors.sync.SyncWorkouts
import com.mikolove.core.domain.analytics.HistoryExerciseCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetCacheDataSource
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSetNetworkDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutCacheDataSource
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.analytics.HistoryWorkoutNetworkDataSource
import com.mikolove.core.domain.bodypart.BodyPartCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.exercise.ExerciseSetNetworkDataSource
import com.mikolove.core.domain.user.UserCacheDataSource
import com.mikolove.core.domain.user.UserFactory
import com.mikolove.core.domain.user.UserNetworkDataSource
import com.mikolove.core.domain.workout.GroupCacheDataSource
import com.mikolove.core.domain.workout.GroupNetworkDataSource
import com.mikolove.core.interactors.exercise.ExerciseInteractors
import com.mikolove.core.interactors.exercise.GetExerciseOrderAndFilter
import com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise
import com.mikolove.core.interactors.exercise.InsertExercise
import com.mikolove.core.interactors.exercise.InsertExerciseSet
import com.mikolove.core.interactors.exercise.InsertMultipleExerciseSet
import com.mikolove.core.interactors.exercise.RemoveExerciseSet
import com.mikolove.core.interactors.exercise.RemoveMultipleExerciseSet
import com.mikolove.core.interactors.exercise.RemoveMultipleExercises
import com.mikolove.core.interactors.exercise.UpdateExercise
import com.mikolove.core.interactors.exercise.UpdateExerciseSet
import com.mikolove.core.interactors.exercise.UpdateMultipleExerciseSet
import com.mikolove.core.interactors.exercise.UpdateNetworkExerciseSets
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeCacheDataSource
import com.mikolove.core.domain.workouttype.WorkoutTypeNetworkDataSource
import com.mikolove.core.interactors.workout.AddExerciseToWorkout
import com.mikolove.core.interactors.workout.GetTotalWorkouts
import com.mikolove.core.interactors.workout.GetWorkoutOrderAndFilter
import com.mikolove.core.interactors.workout.GetWorkouts
import com.mikolove.core.interactors.workout.InsertWorkout
import com.mikolove.core.interactors.workout.RemoveExerciseFromWorkout
import com.mikolove.core.interactors.workout.RemoveMultipleWorkouts
import com.mikolove.core.interactors.workout.RemoveWorkout
import com.mikolove.core.interactors.workout.UpdateWorkout
import com.mikolove.core.interactors.workout.WorkoutInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {


    @Singleton
    @Provides
    fun provideWorkoutInteractors(
        workoutCacheDataSource : WorkoutCacheDataSource,
        exerciseCacheDataSource : ExerciseCacheDataSource,
        bodyPartCacheDataSource : BodyPartCacheDataSource,
        workoutTypeCacheDataSource : WorkoutTypeCacheDataSource,
        workoutNetworkDataSource : WorkoutNetworkDataSource,
        exerciseNetworkDataSource : ExerciseNetworkDataSource,
        appDataStore: AppDataStore,
        workoutFactory: WorkoutFactory,
        dateUtil: DateUtil
    ) : com.mikolove.core.interactors.workout.WorkoutInteractors {
        return com.mikolove.core.interactors.workout.WorkoutInteractors(
            getWorkouts = com.mikolove.core.interactors.workout.GetWorkouts(workoutCacheDataSource),
            getExercises = com.mikolove.core.interactors.common.GetExercises(exerciseCacheDataSource),
            getWorkoutById = com.mikolove.core.interactors.common.GetWorkoutById(
                workoutCacheDataSource
            ),
            getTotalWorkouts = com.mikolove.core.interactors.workout.GetTotalWorkouts(
                workoutCacheDataSource
            ),
            getTotalExercises = com.mikolove.core.interactors.common.GetTotalExercises(
                exerciseCacheDataSource
            ),
            insertWorkout = com.mikolove.core.interactors.workout.InsertWorkout(
                workoutCacheDataSource,
                workoutNetworkDataSource,
                workoutFactory
            ),
            updateWorkout = com.mikolove.core.interactors.workout.UpdateWorkout(
                workoutCacheDataSource,
                workoutNetworkDataSource
            ),
            removeWorkout = com.mikolove.core.interactors.workout.RemoveWorkout(
                workoutCacheDataSource,
                workoutNetworkDataSource
            ),
            removeMultipleWorkouts = com.mikolove.core.interactors.workout.RemoveMultipleWorkouts(
                workoutCacheDataSource,
                workoutNetworkDataSource
            ),
            addExerciseToWorkout = com.mikolove.core.interactors.workout.AddExerciseToWorkout(
                workoutCacheDataSource,
                workoutNetworkDataSource,
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                dateUtil
            ),
            removeExerciseFromWorkout = com.mikolove.core.interactors.workout.RemoveExerciseFromWorkout(
                workoutCacheDataSource,
                workoutNetworkDataSource,
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                dateUtil
            ),
            getWorkoutTypes = com.mikolove.core.interactors.common.GetWorkoutTypes(
                workoutTypeCacheDataSource
            ),
            getBodyParts = com.mikolove.core.interactors.common.GetBodyParts(bodyPartCacheDataSource),
            getTotalBodyParts = com.mikolove.core.interactors.common.GetTotalBodyParts(
                bodyPartCacheDataSource
            ),
            getTotalBodyPartsByWorkoutType = com.mikolove.core.interactors.common.GetTotalBodyPartsByWorkoutType(
                bodyPartCacheDataSource
            ),
            getWorkoutOrderAndFilter = com.mikolove.core.interactors.workout.GetWorkoutOrderAndFilter(
                appDataStoreManager = appDataStore
            )
        )
    }

    @Singleton
    @Provides
    fun provideExerciseInteractors(
        exerciseCacheDataSource : ExerciseCacheDataSource,
        bodyPartCacheDataSource : BodyPartCacheDataSource,
        workoutTypeCacheDataSource : WorkoutTypeCacheDataSource,
        exerciseNetworkDataSource : ExerciseNetworkDataSource,
        exerciseFactory: ExerciseFactory,
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
        exerciseSetFactory: ExerciseSetFactory,
        appDataStore: AppDataStore
    ) : com.mikolove.core.interactors.exercise.ExerciseInteractors {
        return com.mikolove.core.interactors.exercise.ExerciseInteractors(
            getExercises = com.mikolove.core.interactors.common.GetExercises(exerciseCacheDataSource),
            getExerciseById = com.mikolove.core.interactors.common.GetExerciseById(
                exerciseCacheDataSource
            ),
            getExerciseSetByIdExercise = com.mikolove.core.interactors.exercise.GetExerciseSetByIdExercise(
                exerciseSetCacheDataSource
            ),
            getTotalExercises = com.mikolove.core.interactors.common.GetTotalExercises(
                exerciseCacheDataSource
            ),
            insertExercise = com.mikolove.core.interactors.exercise.InsertExercise(
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                exerciseFactory,
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource,
                exerciseSetFactory
            ),
            insertExerciseSet = com.mikolove.core.interactors.exercise.InsertExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource,
                exerciseSetFactory
            ),
            insertMultipleExerciseSet = com.mikolove.core.interactors.exercise.InsertMultipleExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            updateMultipleExerciseSet = com.mikolove.core.interactors.exercise.UpdateMultipleExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            removeMultipleExerciseSet = com.mikolove.core.interactors.exercise.RemoveMultipleExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            updateNetworkExerciseSets = com.mikolove.core.interactors.exercise.UpdateNetworkExerciseSets(
                exerciseSetNetworkDataSource
            ),
            updateExercise = com.mikolove.core.interactors.exercise.UpdateExercise(
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            updateExerciseSet = com.mikolove.core.interactors.exercise.UpdateExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            //removeExercise = RemoveExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            removeExerciseSet = com.mikolove.core.interactors.exercise.RemoveExerciseSet(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            removeMultipleExercises = com.mikolove.core.interactors.exercise.RemoveMultipleExercises(
                exerciseCacheDataSource,
                exerciseNetworkDataSource
            ),
            getWorkoutTypes = com.mikolove.core.interactors.common.GetWorkoutTypes(
                workoutTypeCacheDataSource
            ),
            getBodyParts = com.mikolove.core.interactors.common.GetBodyParts(bodyPartCacheDataSource),
            getBodyPartsByWorkoutType = com.mikolove.core.interactors.common.GetBodyPartsByWorkoutType(
                bodyPartCacheDataSource
            ),
            getTotalBodyParts = com.mikolove.core.interactors.common.GetTotalBodyParts(
                bodyPartCacheDataSource
            ),
            getTotalBodyPartsByWorkoutType = com.mikolove.core.interactors.common.GetTotalBodyPartsByWorkoutType(
                bodyPartCacheDataSource
            ),
            getExerciseOrderAndFilter = com.mikolove.core.interactors.exercise.GetExerciseOrderAndFilter(
                appDataStoreManager = appDataStore
            )

        )
    }

    @Singleton
    @Provides
    fun provideInProgressListInteractors(
        workoutCacheDataSource : WorkoutCacheDataSource,
        exerciseCacheDataSource : ExerciseCacheDataSource,
        historyWorkoutFactory : HistoryWorkoutFactory,
        historyExerciseFactory : HistoryExerciseFactory,
        historyExerciseSetFactory : HistoryExerciseSetFactory,
        historyWorkoutNetworkDataSource : HistoryWorkoutNetworkDataSource,
        historyExerciseNetworkDataSource : HistoryExerciseNetworkDataSource,
        historyExerciseSetNetworkDataSource : HistoryExerciseSetNetworkDataSource,
        historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource,
        historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
        historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
        workoutTypeCacheDataSource: WorkoutTypeCacheDataSource
    ): com.mikolove.core.interactors.workoutinprogress.InProgressListInteractors {

        return com.mikolove.core.interactors.workoutinprogress.InProgressListInteractors(
            getWorkoutById = com.mikolove.core.interactors.common.GetWorkoutById(
                workoutCacheDataSource
            ),
            getExerciseById = com.mikolove.core.interactors.common.GetExerciseById(
                exerciseCacheDataSource
            ),
            insertHistory = com.mikolove.core.interactors.workoutinprogress.InsertHistory(
                historyWorkoutCacheDataSource,
                historyExerciseCacheDataSource,
                historyExerciseSetCacheDataSource,
                historyWorkoutNetworkDataSource,
                historyExerciseNetworkDataSource,
                historyExerciseSetNetworkDataSource,
                workoutTypeCacheDataSource,
                historyWorkoutFactory,
                historyExerciseFactory,
                historyExerciseSetFactory
            )
        )
    }

    @Singleton
    @Provides
    fun provideHistoryListInteractors(
        historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
    ) : com.mikolove.core.interactors.analytics.HistoryListInteractors {
        return com.mikolove.core.interactors.analytics.HistoryListInteractors(
            getHistoryWorkoutDetail = com.mikolove.core.interactors.analytics.GetHistoryWorkoutDetail(
                historyWorkoutCacheDataSource
            ),
            getHistoryWorkouts = com.mikolove.core.interactors.analytics.GetHistoryWorkouts(
                historyWorkoutCacheDataSource
            ),
            getTotalHistoryWorkouts = com.mikolove.core.interactors.analytics.GetTotalHistoryWorkouts(
                historyWorkoutCacheDataSource
            )
        )
    }

    @Singleton
    @Provides
    fun provideLoadingInteractors(
        userCacheDataSource: UserCacheDataSource,
        userNetworkDataSource: UserNetworkDataSource,
        userFactory: UserFactory,
        dateUtil: DateUtil,
    ) : com.mikolove.core.interactors.loading.LoadingInteractors {
        return com.mikolove.core.interactors.loading.LoadingInteractors(
            loadUser = com.mikolove.core.interactors.loading.LoadUser(
                userCacheDataSource,
                userNetworkDataSource,
                userFactory,
                dateUtil
            ),
            //getAccountPreferences = GetAccountPreferences(appDataStore)
        )
    }

    @Singleton
    @Provides
    fun provideSessionInteractors(
        firebaseAuth: FirebaseAuth,
        userFactory: UserFactory,
    ) : com.mikolove.core.interactors.session.SessionInteractors {
        return com.mikolove.core.interactors.session.SessionInteractors(
            signOut = com.mikolove.core.interactors.auth.SignOut(),
            getAuthState = com.mikolove.core.interactors.auth.GetAuthState(
                firebaseAuth = firebaseAuth,
                userFactory = userFactory
            ),
            //getSessionPreference = GetSessionPreferences(appDataStore),
        )
    }

    @Singleton
    @Provides
    fun provideSyncInteractors(
        connectivityManager: ConnectivityManager,
        @SimpleDateFormatLocal dateFormat: SimpleDateFormat,
        dateUtil: DateUtil,
        bodyPartCacheDataSource : BodyPartCacheDataSource,
        historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource,
        historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
        historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource,
        historyWorkoutNetworkDataSource: HistoryWorkoutNetworkDataSource,
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
        workoutCacheDataSource: WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource,
        workoutTypeCacheDataSource: WorkoutTypeCacheDataSource,
        workoutTypeNetworkDataSource: WorkoutTypeNetworkDataSource,
        groupCacheDataSource: GroupCacheDataSource,
        groupNetworkDataSource: GroupNetworkDataSource
    ) : com.mikolove.core.interactors.sync.SyncInteractors {
        return com.mikolove.core.interactors.sync.SyncInteractors(
            com.mikolove.core.interactors.sync.SyncNetworkConnectivity(connectivityManager = connectivityManager),
            syncDeletedExercises = com.mikolove.core.interactors.sync.SyncDeletedExercises(
                exerciseCacheDataSource,
                exerciseNetworkDataSource
            ),
            syncDeletedExerciseSets = com.mikolove.core.interactors.sync.SyncDeletedExerciseSets(
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            syncDeletedWorkouts = com.mikolove.core.interactors.sync.SyncDeletedWorkouts(
                workoutCacheDataSource,
                workoutNetworkDataSource
            ),
            syncExercises = com.mikolove.core.interactors.sync.SyncExercises(
                dateFormat,
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                exerciseSetCacheDataSource,
                exerciseSetNetworkDataSource
            ),
            syncHistory = com.mikolove.core.interactors.sync.SyncHistory(
                historyWorkoutCacheDataSource,
                historyExerciseCacheDataSource,
                historyExerciseSetCacheDataSource,
                historyWorkoutNetworkDataSource
            ),
            syncWorkouts = com.mikolove.core.interactors.sync.SyncWorkouts(
                dateFormat,
                workoutCacheDataSource,
                workoutNetworkDataSource
            ),
            syncWorkoutExercises = com.mikolove.core.interactors.sync.SyncWorkoutExercises(
                workoutCacheDataSource,
                workoutNetworkDataSource,
                exerciseCacheDataSource,
                exerciseNetworkDataSource,
                dateUtil
            ),
            syncWorkoutTypesAndBodyPart = com.mikolove.core.interactors.sync.SyncWorkoutTypesAndBodyPart(
                workoutTypeCacheDataSource,
                workoutTypeNetworkDataSource,
                bodyPartCacheDataSource
            ),
            syncWorkoutGroups = com.mikolove.core.interactors.sync.SyncWorkoutGroups(
                groupCacheDataSource,
                groupNetworkDataSource,
                dateUtil
            )
        )
    }
}