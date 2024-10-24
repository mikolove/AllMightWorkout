package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.AllMightWorkoutApplication
import com.mikolove.allmightworkout.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope>{
        (androidApplication() as AllMightWorkoutApplication).applicationScope
    }

    viewModelOf(::MainViewModel)
}