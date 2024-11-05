package com.mikolove.allmightworkout.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikolove.allmightworkout.AllMightWorkoutApplication
import com.mikolove.allmightworkout.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val appModule = module {

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

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