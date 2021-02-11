package com.mikolove.allmightworkout.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikolove.allmightworkout.framework.datasource.cache.database.AllMightWorkoutDatabase
import com.mikolove.allmightworkout.framework.datasource.preferences.PreferenceKeys
import com.mikolove.allmightworkout.framework.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideAllMightDatabase(@ApplicationContext context: Context) : AllMightWorkoutDatabase{
        return Room
            .databaseBuilder(
                context,
                AllMightWorkoutDatabase::class.java,
                AllMightWorkoutDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) : SharedPreferences{
        return context.getSharedPreferences(
            PreferenceKeys.WORKOUT_LIST_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}