package com.mikolove.allmightworkout.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mikolove.allmightworkout.AllMightWorkoutApplication
import com.mikolove.allmightworkout.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val appModule = module {

    single<SharedPreferences> {
        EncryptedSharedPreferences(
            androidApplication(),
            "auth_pref",
            MasterKey(androidApplication()),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single<CoroutineScope>{
        (androidApplication() as AllMightWorkoutApplication).applicationScope
    }

    viewModelOf(::MainViewModel)
}