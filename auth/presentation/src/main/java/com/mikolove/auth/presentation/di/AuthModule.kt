package com.mikolove.auth.presentation.di

import com.mikolove.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {

    viewModelOf(::RegisterViewModel)
}