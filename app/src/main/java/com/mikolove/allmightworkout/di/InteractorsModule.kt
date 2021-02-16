package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkoutDetail
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkouts
import com.mikolove.allmightworkout.business.interactors.main.history.HistoryListInteractors
import com.mikolove.allmightworkout.business.interactors.main.home.*
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.*
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.*
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryExercise
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryExerciseSet
import com.mikolove.allmightworkout.business.interactors.main.workout.InsertHistoryWorkout
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutListInteractors
import com.mikolove.allmightworkout.framework.presentation.main.home.state.HomeViewState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Singleton
    @Provides
    fun provideHomeListInteractors(
        workoutCacheDataSource : WorkoutCacheDataSource,
        exerciseCacheDataSource : ExerciseCacheDataSource,
        bodyPartCacheDataSource : BodyPartCacheDataSource,
        workoutTypeCacheDataSource : WorkoutTypeCacheDataSource,
        workoutNetworkDataSource : WorkoutNetworkDataSource,
        exerciseNetworkDataSource : ExerciseNetworkDataSource
    ) : HomeListInteractors{
        return HomeListInteractors(
            getBodyParts = GetBodyParts(bodyPartCacheDataSource),
            getExercises = GetExercises(exerciseCacheDataSource),
            getTotalBodyParts = GetTotalBodyParts(bodyPartCacheDataSource),
            getTotalBodyPartsByWorkoutType = GetTotalBodyPartsByWorkoutType(bodyPartCacheDataSource),
            getTotalExercises = GetTotalExercises(exerciseCacheDataSource),
            getTotalWorkouts = GetTotalWorkouts(workoutCacheDataSource),
            getWorkoutById = GetWorkoutById(workoutCacheDataSource),
            getWorkouts = GetWorkouts(workoutCacheDataSource),
            getWorkoutTypes = GetWorkoutTypes(workoutTypeCacheDataSource),
            removeMultipleExercises = RemoveMultipleExercises(exerciseCacheDataSource,exerciseNetworkDataSource),
            removeMultipleWorkouts = RemoveMultipleWorkouts(workoutCacheDataSource,workoutNetworkDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideWorkoutListInteractors(
        historyWorkoutFactory : HistoryWorkoutFactory,
        historyExerciseFactory : HistoryExerciseFactory,
        historyExerciseSetFactory : HistoryExerciseSetFactory,
        historyWorkoutCacheDataSource : HistoryWorkoutCacheDataSource,
        historyExerciseCacheDataSource: HistoryExerciseCacheDataSource,
        historyExerciseSetCacheDataSource: HistoryExerciseSetCacheDataSource
    ): WorkoutListInteractors{

        return WorkoutListInteractors(
            insertHistoryWorkout = InsertHistoryWorkout(historyWorkoutCacheDataSource,historyWorkoutFactory),
            insertHistoryExercise = InsertHistoryExercise(historyExerciseCacheDataSource,historyExerciseFactory),
            insertHistoryExerciseSet = InsertHistoryExerciseSet(historyExerciseSetCacheDataSource,historyExerciseSetFactory)
        )
    }

    @Singleton
    @Provides
    fun provideManageWorkoutListInteractors(
        workoutFactory: WorkoutFactory,
        workoutCacheDataSource: WorkoutCacheDataSource,
        workoutNetworkDataSource: WorkoutNetworkDataSource,
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        dateUtil : DateUtil
    ) : ManageWorkoutListInteractors {

        return ManageWorkoutListInteractors(
            getWorkoutById = GetWorkoutById(workoutCacheDataSource),
            addExerciseToWorkout = AddExerciseToWorkout(workoutCacheDataSource,workoutNetworkDataSource,exerciseCacheDataSource,exerciseNetworkDataSource,dateUtil),
            insertWorkout = InsertWorkout(workoutCacheDataSource,workoutNetworkDataSource,workoutFactory),
            removeExerciseFromWorkout = RemoveExerciseFromWorkout(workoutCacheDataSource,workoutNetworkDataSource,exerciseCacheDataSource,exerciseNetworkDataSource,dateUtil),
            removeWorkout = RemoveWorkout(workoutCacheDataSource,workoutNetworkDataSource),
            updateWorkout = UpdateWorkout(workoutCacheDataSource,workoutNetworkDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideManageExerciseListInteractors(
        exerciseCacheDataSource: ExerciseCacheDataSource,
        exerciseNetworkDataSource: ExerciseNetworkDataSource,
        exerciseFactory: ExerciseFactory,
        exerciseSetCacheDataSource: ExerciseSetCacheDataSource,
        exerciseSetNetworkDataSource: ExerciseSetNetworkDataSource,
        exerciseSetFactory: ExerciseSetFactory
    ): ManageExerciseListInteractors {

        return ManageExerciseListInteractors(
            insertExercise = InsertExercise(exerciseCacheDataSource,exerciseNetworkDataSource,exerciseFactory),
            insertExerciseSet = InsertExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource,exerciseSetFactory),
            removeExercise = RemoveExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            removeExerciseSet = RemoveExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            updateExercise = UpdateExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            updateExerciseSet = UpdateExerciseSet(exerciseSetCacheDataSource, exerciseSetNetworkDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideHistoryListInteractors(
        historyWorkoutCacheDataSource: HistoryWorkoutCacheDataSource
    ) : HistoryListInteractors{
        return HistoryListInteractors(
            getHistoryWorkoutDetail = GetHistoryWorkoutDetail(historyWorkoutCacheDataSource),
            getHistoryWorkouts = GetHistoryWorkouts(historyWorkoutCacheDataSource)
        )
    }

}