package com.mikolove.allmightworkout.di

import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseAuth
import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.core.data.datastore.AppDataStore
import com.mikolove.allmightworkout.business.data.network.abstraction.*
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.core.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.auth.GetAuthState
import com.mikolove.allmightworkout.business.interactors.main.auth.SignOut
import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkoutDetail
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkouts
import com.mikolove.allmightworkout.business.interactors.main.history.GetTotalHistoryWorkouts
import com.mikolove.allmightworkout.business.interactors.main.history.HistoryListInteractors
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadUser
import com.mikolove.allmightworkout.business.interactors.main.loading.LoadingInteractors
import com.mikolove.allmightworkout.business.interactors.main.session.SessionInteractors
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.*
import com.mikolove.allmightworkout.business.interactors.sync.SyncDeletedExerciseSets
import com.mikolove.allmightworkout.business.interactors.sync.SyncDeletedExercises
import com.mikolove.allmightworkout.business.interactors.sync.SyncDeletedWorkouts
import com.mikolove.allmightworkout.business.interactors.sync.SyncExercises
import com.mikolove.allmightworkout.business.interactors.sync.SyncHistory
import com.mikolove.allmightworkout.business.interactors.sync.SyncInteractors
import com.mikolove.allmightworkout.business.interactors.sync.SyncNetworkConnectivity
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutExercises
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutGroups
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkoutTypesAndBodyPart
import com.mikolove.allmightworkout.business.interactors.sync.SyncWorkouts
import com.mikolove.core.domain.analytics.HistoryExerciseFactory
import com.mikolove.core.domain.analytics.HistoryExerciseSetFactory
import com.mikolove.core.domain.analytics.HistoryWorkoutFactory
import com.mikolove.core.domain.exercise.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseFactory
import com.mikolove.core.domain.exercise.ExerciseSetCacheDataSource
import com.mikolove.core.domain.exercise.ExerciseSetFactory
import com.mikolove.core.domain.exercise.usecase.ExerciseInteractors
import com.mikolove.core.domain.exercise.usecase.GetExerciseOrderAndFilter
import com.mikolove.core.domain.exercise.usecase.GetExerciseSetByIdExercise
import com.mikolove.core.domain.exercise.usecase.InsertExercise
import com.mikolove.core.domain.exercise.usecase.InsertExerciseSet
import com.mikolove.core.domain.exercise.usecase.InsertMultipleExerciseSet
import com.mikolove.core.domain.exercise.usecase.RemoveExerciseSet
import com.mikolove.core.domain.exercise.usecase.RemoveMultipleExerciseSet
import com.mikolove.core.domain.exercise.usecase.RemoveMultipleExercises
import com.mikolove.core.domain.exercise.usecase.UpdateExercise
import com.mikolove.core.domain.exercise.usecase.UpdateExerciseSet
import com.mikolove.core.domain.exercise.usecase.UpdateMultipleExerciseSet
import com.mikolove.core.domain.exercise.usecase.UpdateNetworkExerciseSets
import com.mikolove.core.domain.workout.WorkoutCacheDataSource
import com.mikolove.core.domain.workout.WorkoutFactory
import com.mikolove.core.domain.workout.WorkoutNetworkDataSource
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
            getExercises = GetExercises(exerciseCacheDataSource),
            getWorkoutById = GetWorkoutById(workoutCacheDataSource),
            getTotalWorkouts = com.mikolove.core.interactors.workout.GetTotalWorkouts(
                workoutCacheDataSource
            ),
            getTotalExercises = GetTotalExercises(exerciseCacheDataSource),
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
            getWorkoutTypes = GetWorkoutTypes(workoutTypeCacheDataSource),
            getBodyParts = GetBodyParts(bodyPartCacheDataSource),
            getTotalBodyParts = GetTotalBodyParts(bodyPartCacheDataSource),
            getTotalBodyPartsByWorkoutType = GetTotalBodyPartsByWorkoutType(bodyPartCacheDataSource),
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
    ) : ExerciseInteractors {
        return ExerciseInteractors(
            getExercises = GetExercises(exerciseCacheDataSource),
            getExerciseById = GetExerciseById(exerciseCacheDataSource),
            getExerciseSetByIdExercise = GetExerciseSetByIdExercise(exerciseSetCacheDataSource),
            getTotalExercises = GetTotalExercises(exerciseCacheDataSource),
            insertExercise = InsertExercise(exerciseCacheDataSource,exerciseNetworkDataSource,exerciseFactory,exerciseSetCacheDataSource,exerciseSetNetworkDataSource,exerciseSetFactory),
            insertExerciseSet = InsertExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource,exerciseSetFactory),
            insertMultipleExerciseSet = InsertMultipleExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            updateMultipleExerciseSet = UpdateMultipleExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            removeMultipleExerciseSet = RemoveMultipleExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            updateNetworkExerciseSets = UpdateNetworkExerciseSets(exerciseSetNetworkDataSource),
            updateExercise = UpdateExercise(exerciseCacheDataSource,exerciseNetworkDataSource,exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            updateExerciseSet = UpdateExerciseSet(exerciseSetCacheDataSource, exerciseSetNetworkDataSource),
            //removeExercise = RemoveExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            removeExerciseSet = RemoveExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            removeMultipleExercises = RemoveMultipleExercises(exerciseCacheDataSource,exerciseNetworkDataSource),
            getWorkoutTypes = GetWorkoutTypes(workoutTypeCacheDataSource),
            getBodyParts = GetBodyParts(bodyPartCacheDataSource),
            getBodyPartsByWorkoutType = GetBodyPartsByWorkoutType(bodyPartCacheDataSource),
            getTotalBodyParts = GetTotalBodyParts(bodyPartCacheDataSource),
            getTotalBodyPartsByWorkoutType = GetTotalBodyPartsByWorkoutType(bodyPartCacheDataSource),
            getExerciseOrderAndFilter = GetExerciseOrderAndFilter(appDataStoreManager = appDataStore)

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
    ): InProgressListInteractors{

        return InProgressListInteractors(
            getWorkoutById = GetWorkoutById(workoutCacheDataSource),
            getExerciseById = GetExerciseById(exerciseCacheDataSource),
            insertHistory = InsertHistory(
                historyWorkoutCacheDataSource,historyExerciseCacheDataSource,historyExerciseSetCacheDataSource,
                historyWorkoutNetworkDataSource,historyExerciseNetworkDataSource, historyExerciseSetNetworkDataSource,
                workoutTypeCacheDataSource,historyWorkoutFactory,historyExerciseFactory,historyExerciseSetFactory)
        )
    }

    @Singleton
    @Provides
    fun provideHistoryListInteractors(
        historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
    ) : HistoryListInteractors{
        return HistoryListInteractors(
            getHistoryWorkoutDetail = GetHistoryWorkoutDetail(historyWorkoutCacheDataSource),
            getHistoryWorkouts = GetHistoryWorkouts(historyWorkoutCacheDataSource),
            getTotalHistoryWorkouts = GetTotalHistoryWorkouts(historyWorkoutCacheDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideLoadingInteractors(
        userCacheDataSource: UserCacheDataSource,
        userNetworkDataSource: UserNetworkDataSource,
        userFactory: UserFactory,
        dateUtil: DateUtil,
    ) : LoadingInteractors{
        return LoadingInteractors(
            loadUser = LoadUser(userCacheDataSource,userNetworkDataSource, userFactory , dateUtil),
            //getAccountPreferences = GetAccountPreferences(appDataStore)
        )
    }

    @Singleton
    @Provides
    fun provideSessionInteractors(
        firebaseAuth: FirebaseAuth,
        userFactory: UserFactory,
    ) : SessionInteractors{
        return SessionInteractors(
            signOut = SignOut(),
            getAuthState = GetAuthState(
                firebaseAuth = firebaseAuth,
                userFactory = userFactory),
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
    ) : SyncInteractors{
        return SyncInteractors(
            SyncNetworkConnectivity(connectivityManager = connectivityManager),
            syncDeletedExercises = SyncDeletedExercises( exerciseCacheDataSource, exerciseNetworkDataSource),
            syncDeletedExerciseSets = SyncDeletedExerciseSets(exerciseSetCacheDataSource, exerciseSetNetworkDataSource),
            syncDeletedWorkouts = SyncDeletedWorkouts(workoutCacheDataSource,workoutNetworkDataSource),
            syncExercises = SyncExercises(dateFormat,exerciseCacheDataSource, exerciseNetworkDataSource, exerciseSetCacheDataSource, exerciseSetNetworkDataSource),
            syncHistory = SyncHistory(historyWorkoutCacheDataSource, historyExerciseCacheDataSource, historyExerciseSetCacheDataSource, historyWorkoutNetworkDataSource),
            syncWorkouts = SyncWorkouts(dateFormat,workoutCacheDataSource, workoutNetworkDataSource),
            syncWorkoutExercises = SyncWorkoutExercises(workoutCacheDataSource,workoutNetworkDataSource,exerciseCacheDataSource,exerciseNetworkDataSource,dateUtil),
            syncWorkoutTypesAndBodyPart = SyncWorkoutTypesAndBodyPart(workoutTypeCacheDataSource,workoutTypeNetworkDataSource, bodyPartCacheDataSource),
            syncWorkoutGroups = SyncWorkoutGroups(groupCacheDataSource, groupNetworkDataSource,dateUtil)
        )
    }
}