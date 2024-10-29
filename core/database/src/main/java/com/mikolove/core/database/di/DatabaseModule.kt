package com.mikolove.core.database.di

import androidx.room.Room
import com.mikolove.core.database.database.AllMightWorkoutDatabase
import com.mikolove.core.database.implementation.AnalyticsDaoService
import com.mikolove.core.database.implementation.BodyPartDaoService
import com.mikolove.core.database.implementation.ExerciseDaoService
import com.mikolove.core.database.implementation.GroupDaoService
import com.mikolove.core.database.implementation.UserDaoService
import com.mikolove.core.database.implementation.WorkoutDaoService
import com.mikolove.core.database.implementation.WorkoutTypeDaoService
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheService
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheService
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheService
import com.mikolove.core.domain.user.abstraction.UserCacheService
import com.mikolove.core.domain.workout.abstraction.GroupCacheService
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheService
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AllMightWorkoutDatabase::class.java,
            "amworkout.db"
        ).build()
    }

    single { get<AllMightWorkoutDatabase>().userDao }
    single { get<AllMightWorkoutDatabase>().workoutDao }
    single { get<AllMightWorkoutDatabase>().exerciseDao }
    single { get<AllMightWorkoutDatabase>().exerciseSetDao }
    single { get<AllMightWorkoutDatabase>().bodyPartDao }
    single { get<AllMightWorkoutDatabase>().workoutTypeDao }
    single { get<AllMightWorkoutDatabase>().analyticsDao }
    single { get<AllMightWorkoutDatabase>().groupDao }
    single { get<AllMightWorkoutDatabase>().workoutGroupDao }
    single { get<AllMightWorkoutDatabase>().exerciseBodyPartDao }

    singleOf(::BodyPartDaoService).bind<BodyPartCacheService>()
    singleOf(::WorkoutTypeDaoService).bind<WorkoutTypeCacheService>()
    singleOf(::UserDaoService).bind<UserCacheService>()
    singleOf(::WorkoutDaoService).bind<WorkoutCacheService>()
    singleOf(::ExerciseDaoService).bind<ExerciseCacheService>()
    singleOf(::GroupDaoService).bind<GroupCacheService>()
    singleOf(::AnalyticsDaoService).bind<AnalyticsCacheService>()

}