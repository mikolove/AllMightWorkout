package com.mikolove.allmightworkout.di

import android.content.Context
import androidx.room.Room
import com.mikolove.allmightworkout.framework.datasource.cache.database.AllMightWorkoutDatabase
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


}