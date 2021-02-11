package com.mikolove.allmightworkout.oldCode

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.interactors.main.history.HistoryListInteractors
import com.mikolove.allmightworkout.business.interactors.main.home.HomeListInteractors
import com.mikolove.allmightworkout.business.interactors.main.manageexercise.ManageExerciseListInteractors
import com.mikolove.allmightworkout.business.interactors.main.manageworkout.ManageWorkoutListInteractors
import com.mikolove.allmightworkout.business.interactors.main.workout.WorkoutListInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

/*    @Singleton
    @Provides
    fun provideViewModelFactory(
        homeListInteractors : HomeListInteractors,
        historyListInteractors : HistoryListInteractors,
        manageExerciseListInteractors : ManageExerciseListInteractors,
        managerWorkoutListInteractors : ManageWorkoutListInteractors,
        workoutListInteractors : WorkoutListInteractors,
        workoutFactory : WorkoutFactory,
        exerciseFactory : ExerciseFactory,
        exerciseSetFactory : ExerciseSetFactory,
        workoutTypeFactory : WorkoutTypeFactory,
        bodyPartFactory : BodyPartFactory,
        historyWorkoutFactory : HistoryWorkoutFactory,
        historyExerciseFactory : HistoryExerciseFactory,
        historyExerciseSetFactory : HistoryExerciseSetFactory,
        editor : SharedPreferences.Editor,
        sharedPreferences : SharedPreferences
    ) : ViewModelProvider.Factory{
        return ViewModelFactory(
            homeListInteractors = homeListInteractors,
            historyListInteractors = historyListInteractors,
            manageExerciseListInteractors = manageExerciseListInteractors,
            managerWorkoutListInteractors = managerWorkoutListInteractors,
            workoutListInteractors = workoutListInteractors,
            workoutFactory = workoutFactory,
            exerciseFactory = exerciseFactory,
            exerciseSetFactory = exerciseSetFactory,
            workoutTypeFactory = workoutTypeFactory,
            bodyPartFactory = bodyPartFactory,
            historyWorkoutFactory = historyWorkoutFactory,
            historyExerciseFactory = historyExerciseFactory,
            historyExerciseSetFactory = historyExerciseSetFactory,
            editor = editor,
            sharedPreferences = sharedPreferences
        )
    }*/
}