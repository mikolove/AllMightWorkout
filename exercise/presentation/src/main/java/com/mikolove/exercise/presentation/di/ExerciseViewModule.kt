package com.mikolove.exercise.presentation.di

import com.mikolove.exercise.presentation.detail.ExerciseDetailViewModel
import com.mikolove.exercise.presentation.overview.ExerciseViewModel
import com.mikolove.exercise.presentation.search.ExerciseSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val exerciseViewModule = module {
    viewModelOf(::ExerciseViewModel)
    viewModelOf(::ExerciseSearchViewModel)
    viewModelOf(::ExerciseDetailViewModel)
}