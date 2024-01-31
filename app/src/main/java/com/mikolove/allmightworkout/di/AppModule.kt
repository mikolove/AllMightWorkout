package com.mikolove.allmightworkout.di

import android.content.SharedPreferences
import com.mikolove.allmightworkout.business.domain.model.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @SimpleDateFormatUS
    @Singleton
    @Provides
    fun provideDateFormatUs() : SimpleDateFormat{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        //sdf.timeZone = TimeZone.getTimeZone("UTC-7") // match firestore
        return sdf
    }

    @SimpleDateFormatLocal
    @Singleton
    @Provides
    fun provideDateFormatLocal() : SimpleDateFormat{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf
    }

    @Singleton
    @Provides
    fun provideDateUtil(
        @SimpleDateFormatUS dateFormatUs: SimpleDateFormat,
        @SimpleDateFormatLocal dateFormatLocal : SimpleDateFormat ) : DateUtil{
        return DateUtil(
            dateFormatUs,
            dateFormatLocal
        )
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SimpleDateFormatUS

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SimpleDateFormatLocal
