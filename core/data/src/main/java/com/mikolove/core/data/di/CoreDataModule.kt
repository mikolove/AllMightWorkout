package com.mikolove.core.data.di

import com.mikolove.core.data.auth.EncryptedSessionStorage
import com.mikolove.core.data.auth.FirebaseSessionManager
import com.mikolove.core.data.datasource.analytics.AnalyticsCacheDataSourceImpl
import com.mikolove.core.data.datasource.analytics.AnalyticsNetworkDataSourceImpl
import com.mikolove.core.data.datasource.bodypart.BodyPartCacheDataSourceImpl
import com.mikolove.core.data.datasource.exercise.ExerciseCacheDataSourceImpl
import com.mikolove.core.data.datasource.exercise.ExerciseSetCacheDataSourceImpl
import com.mikolove.core.data.datasource.exercise.ExerciseNetworkDataSourceImpl
import com.mikolove.core.data.datasource.user.UserCacheDataSourceImpl
import com.mikolove.core.data.datasource.user.UserNetworkDataSourceImpl
import com.mikolove.core.data.datasource.workout.GroupCacheDataSourceImpl
import com.mikolove.core.data.datasource.workout.WorkoutCacheDataSourceImpl
import com.mikolove.core.data.datasource.workouttype.WorkoutTypeCacheDataSourceImpl
import com.mikolove.core.domain.analytics.abstraction.AnalyticsCacheDataSource
import com.mikolove.core.domain.analytics.abstraction.AnalyticsNetworkDataSource
import com.mikolove.core.domain.auth.SessionManager
import com.mikolove.core.domain.auth.SessionStorage
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

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::FirebaseSessionManager).bind<SessionManager>()

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