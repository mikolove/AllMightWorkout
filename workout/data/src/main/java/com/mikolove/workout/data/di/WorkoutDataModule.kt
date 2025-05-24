package com.mikolove.workout.data.di

import com.mikolove.workout.data.OfflineFirstWorkoutRepository
import com.mikolove.workout.domain.WorkoutRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val workoutDataModule = module {
    singleOf(::OfflineFirstWorkoutRepository).bind<WorkoutRepository>()
}