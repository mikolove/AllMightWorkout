package com.mikolove.exercise.data.di

import com.mikolove.exercise.data.OfflineFirstExerciseRepository
import com.mikolove.exercise.domain.ExerciseRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val exerciseDataModule = module {

    singleOf(::OfflineFirstExerciseRepository).bind<ExerciseRepository>()
}