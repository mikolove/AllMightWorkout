package com.mikolove.allmightworkout.di

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.util.RoomDateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDateFormat() : SimpleDateFormat{
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
        //sdf.timeZone = TimeZone.getTimeZone("UTC-7") // match firestore
        return sdf
    }

    @Singleton
    @Provides
    fun provideDateUtil(dateFormat: SimpleDateFormat) : DateUtil{
        return DateUtil(dateFormat)
    }

    @Singleton
    @Provides
    fun provideRomDateUtil(dateFormat: SimpleDateFormat) : RoomDateUtil{
        return RoomDateUtil(dateFormat)
    }


    @Singleton
    @Provides
    fun provideSharedPrefEditor(sharedPreferences: SharedPreferences) : SharedPreferences.Editor{
        return sharedPreferences.edit()
    }

    @Singleton
    @Provides
    fun provideWorkoutFactory(dateUtil: DateUtil) : WorkoutFactory{
        return WorkoutFactory(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseFactory(dateUtil: DateUtil) : ExerciseFactory {
        return ExerciseFactory(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseSetFactory(dateUtil: DateUtil) : ExerciseSetFactory{
        return ExerciseSetFactory(dateUtil)
    }

    @Singleton
    @Provides
    fun provideBodyPartFactory() : BodyPartFactory{
        return BodyPartFactory()
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeFactory() : WorkoutTypeFactory{
        return WorkoutTypeFactory()
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutFactory(dateUtil: DateUtil) : HistoryWorkoutFactory{
        return HistoryWorkoutFactory(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseFactory(dateUtil: DateUtil) : HistoryExerciseFactory{
        return HistoryExerciseFactory(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetFactory(dateUtil: DateUtil) : HistoryExerciseSetFactory{
        return HistoryExerciseSetFactory(dateUtil)
    }

}