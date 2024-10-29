package com.mikolove.auth.data.di

import com.mikolove.auth.data.EmailPatternValidator
import com.mikolove.auth.data.AuthRepositoryImpl
import com.mikolove.auth.domain.AuthRepository
import com.mikolove.auth.domain.PatternValidator
import com.mikolove.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}