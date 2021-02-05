package com.mikolove.allmightworkout.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.mikolove.allmightworkout.business.domain.model.WorkoutTypeFactory
import com.mikolove.allmightworkout.framework.datasource.cache.database.AllMightWorkoutDatabase
import com.mikolove.allmightworkout.framework.datasource.data.WorkoutTypeDataFactory
import com.mikolove.allmightworkout.util.printLogD
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Singleton
    @Provides
    fun provideAllMightDatabase(@ApplicationContext context: Context) : AllMightWorkoutDatabase {
        return Room
            .inMemoryDatabaseBuilder(
                context,
                AllMightWorkoutDatabase::class.java,
            )
            .fallbackToDestructiveMigration()
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
}