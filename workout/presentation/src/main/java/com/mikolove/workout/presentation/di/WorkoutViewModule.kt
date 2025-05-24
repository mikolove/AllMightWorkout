package com.mikolove.workout.presentation.di

import com.mikolove.workout.presentation.overview.WorkoutViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val workoutViewModule = module {
    viewModelOf(::WorkoutViewModel)
}