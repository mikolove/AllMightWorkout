package com.mikolove.core.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkService
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkService
import com.mikolove.core.domain.user.abstraction.UserNetworkService
import com.mikolove.core.domain.workout.abstraction.GroupNetworkService
import com.mikolove.core.domain.workout.abstraction.WorkoutNetworkService
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeNetworkService
import com.mikolove.core.network.firebase.implementation.AnalyticsFirestoreService
import com.mikolove.core.network.firebase.implementation.ExerciseFirestoreService
import com.mikolove.core.network.firebase.implementation.GroupFirestoreService
import com.mikolove.core.network.firebase.implementation.UserFirestoreService
import com.mikolove.core.network.firebase.implementation.WorkoutFirestoreService
import com.mikolove.core.network.firebase.implementation.WorkoutTypeFirestoreService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {

    single<FirebaseFirestore> {
        val db = Firebase.firestore
        db.firestoreSettings = firestoreSettings {
            setLocalCacheSettings(persistentCacheSettings {})
        }
        db
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

    singleOf(::AnalyticsFirestoreService).bind<AnalyticsNetworkService>()
    singleOf(::ExerciseFirestoreService).bind<ExerciseNetworkService>()
    singleOf(::GroupFirestoreService).bind<GroupNetworkService>()
    singleOf(::UserFirestoreService).bind<UserNetworkService>()
    singleOf(::WorkoutFirestoreService).bind<WorkoutNetworkService>()
    singleOf(::WorkoutTypeFirestoreService).bind<WorkoutTypeNetworkService>()

}