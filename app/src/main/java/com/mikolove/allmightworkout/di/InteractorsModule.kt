package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.ExerciseSetNetworkDataSource
import com.mikolove.allmightworkout.business.data.network.abstraction.WorkoutNetworkDataSource
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.business.interactors.main.common.*
import com.mikolove.allmightworkout.business.interactors.main.exercise.*
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkoutDetail
import com.mikolove.allmightworkout.business.interactors.main.history.GetHistoryWorkouts
import com.mikolove.allmightworkout.business.interactors.main.history.HistoryListInteractors
import com.mikolove.allmightworkout.business.interactors.main.workout.*
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExercise
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryExerciseSet
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.InsertHistoryWorkout
import com.mikolove.allmightworkout.business.interactors.main.workoutinprogress.WorkoutInProgressListInteractors
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
    fun provideWorkoutInteractors(
        workoutCacheDataSource : WorkoutCacheDataSource,
        exerciseCacheDataSource : ExerciseCacheDataSource,
        bodyPartCacheDataSource : BodyPartCacheDataSource,
        workoutTypeCacheDataSource : WorkoutTypeCacheDataSource,
        workoutNetworkDataSource : WorkoutNetworkDataSource,
        exerciseNetworkDataSource : ExerciseNetworkDataSource,
        workoutFactory: WorkoutFactory,
        dateUtil: DateUtil
    ) : WorkoutInteractors {
        return WorkoutInteractors(
            getWorkouts = GetWorkouts(workoutCacheDataSource),
            getWorkoutById = GetWorkoutById(workoutCacheDataSource),
            getTotalWorkouts = GetTotalWorkouts(workoutCacheDataSource),
            insertWorkout = InsertWorkout(workoutCacheDataSource,workoutNetworkDataSource,workoutFactory),
            updateWorkout = UpdateWorkout(workoutCacheDataSource,workoutNetworkDataSource),
            removeWorkout = RemoveWorkout(workoutCacheDataSource,workoutNetworkDataSource),
            removeMultipleWorkouts = RemoveMultipleWorkouts(workoutCacheDataSource,workoutNetworkDataSource),
            addExerciseToWorkout = AddExerciseToWorkout(workoutCacheDataSource,workoutNetworkDataSource,exerciseCacheDataSource,exerciseNetworkDataSource,dateUtil),
            removeExerciseFromWorkout = RemoveExerciseFromWorkout(workoutCacheDataSource,workoutNetworkDataSource,exerciseCacheDataSource,exerciseNetworkDataSource,dateUtil),
            getWorkoutTypes = GetWorkoutTypes(workoutTypeCacheDataSource),
            getBodyParts = GetBodyParts(bodyPartCacheDataSource),
            getTotalBodyParts = GetTotalBodyParts(bodyPartCacheDataSource),
            getTotalBodyPartsByWorkoutType = GetTotalBodyPartsByWorkoutType(bodyPartCacheDataSource)
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
    ) : ExerciseInteractors{
        return ExerciseInteractors(
            getExercises = GetExercises(exerciseCacheDataSource),
            getExerciseById = GetExerciseById(exerciseCacheDataSource),
            getExerciseSetByIdExercise = GetExerciseSetByIdExercise(exerciseSetCacheDataSource),
            getTotalExercises = GetTotalExercises(exerciseCacheDataSource),
            insertExercise = InsertExercise(exerciseCacheDataSource,exerciseNetworkDataSource,exerciseFactory),
            insertExerciseSet = InsertExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource,exerciseSetFactory),
            insertMultipleExerciseSet = InsertMultipleExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            updateExercise = UpdateExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            updateExerciseSet = UpdateExerciseSet(exerciseSetCacheDataSource, exerciseSetNetworkDataSource),
            removeExercise = RemoveExercise(exerciseCacheDataSource,exerciseNetworkDataSource),
            removeExerciseSet = RemoveExerciseSet(exerciseSetCacheDataSource,exerciseSetNetworkDataSource),
            removeMultipleExercises = RemoveMultipleExercises(exerciseCacheDataSource,exerciseNetworkDataSource),
            getWorkoutTypes = GetWorkoutTypes(workoutTypeCacheDataSource),
            getBodyParts = GetBodyParts(bodyPartCacheDataSource),
            getBodyPartsByWorkoutType = GetBodyPartsByWorkoutType(bodyPartCacheDataSource),
            getTotalBodyParts = GetTotalBodyParts(bodyPartCacheDataSource),
            getTotalBodyPartsByWorkoutType = GetTotalBodyPartsByWorkoutType(bodyPartCacheDataSource)
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
    ): WorkoutInProgressListInteractors{

        return WorkoutInProgressListInteractors(
            insertHistoryWorkout = InsertHistoryWorkout(historyWorkoutCacheDataSource,historyWorkoutFactory),
            insertHistoryExercise = InsertHistoryExercise(historyExerciseCacheDataSource,historyExerciseFactory),
            insertHistoryExerciseSet = InsertHistoryExerciseSet(historyExerciseSetCacheDataSource,historyExerciseSetFactory)
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