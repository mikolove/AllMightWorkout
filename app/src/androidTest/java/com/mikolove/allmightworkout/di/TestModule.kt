package com.mikolove.allmightworkout.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.mikolove.core.data.datastore.AppDataStore
import com.mikolove.core.data.datastore.AppDataStoreManager
import com.mikolove.allmightworkout.business.domain.model.WorkoutTypeFactory
import com.mikolove.allmightworkout.framework.datasource.cache.database.AllMightWorkoutDatabase
import com.mikolove.allmightworkout.framework.datasource.data.WorkoutTypeDataFactory
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class])
object TestModule {

    @Singleton
    @Provides
    fun provideAllMightDatabase(@ApplicationContext context: Context) : AllMightWorkoutDatabase {
        return Room
            .inMemoryDatabaseBuilder(
                context,
                AllMightWorkoutDatabase::class.java,
            )
            //.fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFirestoreSettings() : FirebaseFirestoreSettings {
        return FirebaseFirestoreSettings.Builder()
            .setHost("10.0.2.2:8080")
            .setSslEnabled(false)
            .setPersistenceEnabled(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(settings: FirebaseFirestoreSettings) : FirebaseFirestore {

        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = settings
        return firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeDataFactory(
        @ApplicationContext context: Context,
        workoutTypeFactory : WorkoutTypeFactory) : WorkoutTypeDataFactory{

        return WorkoutTypeDataFactory(
            application = context,
            workoutTypeFactory = workoutTypeFactory
        )

    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(
            PreferenceKeys.WORKOUT_LIST_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(
        @ApplicationContext context: Context
    ): AppDataStore {
        return AppDataStoreManager(context)
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ) : ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}