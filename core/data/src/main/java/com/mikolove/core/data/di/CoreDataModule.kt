package com.mikolove.core.data.di

import com.mikolove.core.data.repositories.analytics.AnalyticsCacheDataSourceImpl
import com.mikolove.core.data.repositories.analytics.AnalyticsNetworkDataSourceImpl
import com.mikolove.core.data.repositories.bodypart.BodyPartCacheDataSourceImpl
import com.mikolove.core.data.repositories.exercise.ExerciseCacheDataSourceImpl
import com.mikolove.core.data.repositories.exercise.ExerciseSetCacheDataSourceImpl
import com.mikolove.core.data.repositories.exercise.ExerciseNetworkDataSourceImpl
import com.mikolove.core.data.repositories.user.UserCacheDataSourceImpl
import com.mikolove.core.data.repositories.user.UserNetworkDataSourceImpl
import com.mikolove.core.data.repositories.workout.GroupCacheDataSourceImpl
import com.mikolove.core.data.repositories.workout.WorkoutCacheDataSourceImpl
import com.mikolove.core.data.repositories.workouttype.WorkoutTypeCacheDataSourceImpl
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkDataSource
import com.mikolove.core.domain.bodypart.abstraction.BodyPartCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseCacheDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseNetworkDataSource
import com.mikolove.core.domain.exercise.abstraction.ExerciseSetCacheDataSource
import com.mikolove.core.domain.user.abstraction.UserCacheDataSource
import com.mikolove.core.domain.user.abstraction.UserNetworkDataSource
import com.mikolove.core.domain.workout.abstraction.GroupCacheDataSource
import com.mikolove.core.domain.workout.abstraction.WorkoutCacheDataSource
import com.mikolove.core.domain.workouttype.abstraction.WorkoutTypeCacheDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {


    singleOf(::AnalyticsCacheDataSourceImpl).bind<AnalyticsCacheDataSource>()
    singleOf(::AnalyticsNetworkDataSourceImpl).bind<AnalyticsNetworkDataSource>()
    singleOf(::BodyPartCacheDataSourceImpl).bind<BodyPartCacheDataSource>()
    singleOf(::ExerciseCacheDataSourceImpl).bind<ExerciseCacheDataSource>()
    singleOf(::ExerciseSetCacheDataSourceImpl).bind<ExerciseSetCacheDataSource>()
    singleOf(::ExerciseNetworkDataSourceImpl).bind<ExerciseNetworkDataSource>()
    singleOf(::UserCacheDataSourceImpl).bind<UserCacheDataSource>()
    singleOf(::UserNetworkDataSourceImpl).bind<UserNetworkDataSource>()
    singleOf(::GroupCacheDataSourceImpl).bind<GroupCacheDataSource>()
    singleOf(::WorkoutCacheDataSourceImpl).bind<WorkoutCacheDataSource>()
    singleOf(::WorkoutTypeCacheDataSourceImpl).bind<WorkoutTypeCacheDataSource>()

}