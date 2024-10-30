package com.mikolove.auth.presentation.di

import com.mikolove.auth.presentation.login.LoginViewModel
import com.mikolove.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModule = module {

    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}