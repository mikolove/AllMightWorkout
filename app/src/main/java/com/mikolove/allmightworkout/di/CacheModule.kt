package com.mikolove.allmightworkout.di

import com.mikolove.allmightworkout.business.data.cache.abstraction.*
import com.mikolove.allmightworkout.business.data.cache.implementation.*
import com.mikolove.allmightworkout.business.domain.util.DateUtil
import com.mikolove.allmightworkout.framework.datasource.cache.abstraction.*
import com.mikolove.allmightworkout.framework.datasource.cache.database.*
import com.mikolove.allmightworkout.framework.datasource.cache.implementation.*
import com.mikolove.allmightworkout.framework.datasource.cache.mappers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    /*
        DAO
     */

    @Singleton
    @Provides
    fun provideWorkoutDao(allMightDatabase : AllMightWorkoutDatabase) : WorkoutDao{
        return allMightDatabase.workoutDao()
    }

    @Singleton
    @Provides
    fun provideExerciseDao(allMightDatabase: AllMightWorkoutDatabase) : ExerciseDao{
        return allMightDatabase.exerciseDao()
    }

    @Singleton
    @Provides
    fun provideExerciseSetDao(allMightDatabase: AllMightWorkoutDatabase) : ExerciseSetDao{
        return allMightDatabase.exerciseSetDao()
    }

    @Singleton
    @Provides
    fun provideBodyPartDao(allMightDatabase: AllMightWorkoutDatabase) : BodyPartDao{
        return allMightDatabase.bodyPartDao()
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeDao(allMightDatabase: AllMightWorkoutDatabase) : WorkoutTypeDao{
        return allMightDatabase.workoutTypeDao()
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutDao(allMightDatabase: AllMightWorkoutDatabase) : HistoryWorkoutDao{
        return allMightDatabase.historyWorkoutDao()
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseDao(allMightDatabase: AllMightWorkoutDatabase) : HistoryExerciseDao{
        return allMightDatabase.historyExerciseDao()
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetDao(allMightDatabase: AllMightWorkoutDatabase) : HistoryExerciseSetDao{
        return allMightDatabase.historyExerciseSetDao()
    }

    @Singleton
    @Provides
    fun provideWorkoutExerciseDao(allMightDatabase: AllMightWorkoutDatabase) : WorkoutExerciseDao {
        return allMightDatabase.workoutExerciseDao()
    }


    /*
        Cache Mapper
     */

    @Singleton
    @Provides
    fun provideWorkoutTypeCacheMapper() : WorkoutTypeCacheMapper {
        return WorkoutTypeCacheMapper()
    }

    @Singleton
    @Provides
    fun provideBodyPartCacheMapper() : BodyPartCacheMapper{
        return BodyPartCacheMapper()
    }

    @Singleton
    @Provides
    fun provideWorkoutCacheMapper(dateUtil: DateUtil) : WorkoutCacheMapper{
        return WorkoutCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseCacheMapper(dateUtil: DateUtil) : ExerciseCacheMapper {
        return ExerciseCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideExerciseSetCacheMapper(dateUtil: DateUtil) : ExerciseSetCacheMapper {
        return ExerciseSetCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutCacheMapper(dateUtil: DateUtil) : HistoryWorkoutCacheMapper {
        return HistoryWorkoutCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseCacheMapper(dateUtil: DateUtil) : HistoryExerciseCacheMapper {
        return HistoryExerciseCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetCacheMapper(dateUtil: DateUtil) : HistoryExerciseSetCacheMapper {
        return HistoryExerciseSetCacheMapper(dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseWithSetsCacheMapper(historyExerciseCacheMapper: HistoryExerciseCacheMapper, historyExerciseSetCacheMapper : HistoryExerciseSetCacheMapper) : HistoryExerciseWithSetsCacheMapper{
        return HistoryExerciseWithSetsCacheMapper(historyExerciseCacheMapper,historyExerciseSetCacheMapper)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutWithExercisesCacheMapper(historyWorkoutCacheMapper : HistoryWorkoutCacheMapper,historyExerciseWithSetsCacheMapper : HistoryExerciseWithSetsCacheMapper) : HistoryWorkoutWithExercisesCacheMapper{
        return HistoryWorkoutWithExercisesCacheMapper(historyWorkoutCacheMapper,historyExerciseWithSetsCacheMapper)
    }

    @Singleton
    @Provides
    fun provideExerciseWithSetsCacheMapper(bodyPartCacheMapper: BodyPartCacheMapper,exerciseCacheMapper: ExerciseCacheMapper,exerciseSetCacheMapper: ExerciseSetCacheMapper) : ExerciseWithSetsCacheMapper{
        return ExerciseWithSetsCacheMapper(bodyPartCacheMapper,exerciseCacheMapper,exerciseSetCacheMapper)
    }
    @Singleton
    @Provides
    fun provideWorkoutWithExerciseCacheMapper(workoutCacheMapper: WorkoutCacheMapper,exerciseWithSetsCacheMapper : ExerciseWithSetsCacheMapper) : WorkoutWithExercisesCacheMapper{
        return WorkoutWithExercisesCacheMapper(workoutCacheMapper,exerciseWithSetsCacheMapper)
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeWithBodyPartCacheMapper(workoutTypeCacheMapper : WorkoutTypeCacheMapper ,bodyPartCacheMapper : BodyPartCacheMapper): WorkoutTypeWithBodyPartCacheMapper{
        return  WorkoutTypeWithBodyPartCacheMapper(workoutTypeCacheMapper,bodyPartCacheMapper)
    }


    /*
        Dao service
     */


    @Singleton
    @Provides
    fun provideWorkoutDaoService(workoutDao : WorkoutDao,workoutCacheMapper: WorkoutCacheMapper, workoutWithExercisesCacheMapper: WorkoutWithExercisesCacheMapper, dateUtil: DateUtil) : WorkoutDaoService{
        return WorkoutDaoServiceImpl(workoutDao,workoutCacheMapper,workoutWithExercisesCacheMapper,dateUtil)
    }

    @Singleton
    @Provides
    fun provideWorkoutTypeDaoService(workoutTypeDao : WorkoutTypeDao ,workoutTypeCacheMapper : WorkoutTypeCacheMapper ,workoutTypeWithBodyPartCacheMapper : WorkoutTypeWithBodyPartCacheMapper) : WorkoutTypeDaoService{
        return WorkoutTypeDaoServiceImpl(workoutTypeDao,workoutTypeCacheMapper,workoutTypeWithBodyPartCacheMapper)
    }

    @Singleton
    @Provides
    fun provideBodyPartDaoService(bodyPartDao : BodyPartDao, bodyPartCacheMapper : BodyPartCacheMapper) : BodyPartDaoService{
        return BodyPartDaoServiceImpl(bodyPartDao, bodyPartCacheMapper)
    }

    @Singleton
    @Provides
    fun provideExerciseDaoService(exerciseDao : ExerciseDao, exerciseCacheMapper: ExerciseCacheMapper, exerciseWithSetsCacheMapper: ExerciseWithSetsCacheMapper, dateUtil: DateUtil) : ExerciseDaoService{
        return ExerciseDaoServiceImpl(exerciseDao, exerciseCacheMapper, exerciseWithSetsCacheMapper, dateUtil )
    }
    @Singleton
    @Provides
    fun provideWorkoutExerciseDaoService(workoutExerciseDao : WorkoutExerciseDao) : WorkoutExerciseDaoService{
        return WorkoutExerciseDaoServiceImpl(workoutExerciseDao)
    }

    @Singleton
    @Provides
    fun provideExerciseSetDaoService(exerciseSetDao : ExerciseSetDao, exerciseSetCacheMapper : ExerciseSetCacheMapper , dateUtil: DateUtil) : ExerciseSetDaoService{
        return ExerciseSetDaoServiceImpl(exerciseSetDao, exerciseSetCacheMapper, dateUtil)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutDaoService(historyWorkoutDao : HistoryWorkoutDao, historyWorkoutCacheMapper: HistoryWorkoutCacheMapper, historyWorkoutWithExercisesCacheMapper: HistoryWorkoutWithExercisesCacheMapper) : HistoryWorkoutDaoService{
        return HistoryWorkoutDaoServiceImpl(historyWorkoutDao  ,historyWorkoutCacheMapper  ,historyWorkoutWithExercisesCacheMapper )
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseDaoService(historyExerciseDao :HistoryExerciseDao , historyExerciseCacheMapper : HistoryExerciseCacheMapper, historyExerciseWithSetsCacheMapper : HistoryExerciseWithSetsCacheMapper) : HistoryExerciseDaoService{
        return HistoryExerciseDaoServiceImpl(historyExerciseDao , historyExerciseCacheMapper , historyExerciseWithSetsCacheMapper )
    }


    @Singleton
    @Provides
    fun provideHistoryExerciseSetDaoService(historyExerciseSetDao : HistoryExerciseSetDao , historyExerciseSetCacheMapper: HistoryExerciseSetCacheMapper) : HistoryExerciseSetDaoService{
        return HistoryExerciseSetDaoServiceImpl(historyExerciseSetDao  ,historyExerciseSetCacheMapper)
    }

    /*
        Data Source
     */

    @Singleton
    @Provides
    fun provideWorkoutTypeDataSource(workoutTypeDaoService : WorkoutTypeDaoService) : WorkoutTypeCacheDataSource{
        return WorkoutTypeCacheDataSourceImpl(workoutTypeDaoService)
    }
    @Singleton
    @Provides
    fun provideBodyPartCacheDataSource(bodyPartDaoService : BodyPartDaoService) : BodyPartCacheDataSource{
        return BodyPartCacheDataSourceImpl(bodyPartDaoService)
    }

    @Singleton
    @Provides
    fun provideWorkoutCacheDataSource(workoutDaoService : WorkoutDaoService): WorkoutCacheDataSource{
        return WorkoutCacheDataSourceImpl(workoutDaoService)
    }

    @Singleton
    @Provides
    fun provideExerciseCacheDataSource(exerciseDaoService : ExerciseDaoService, workoutExerciseDaoService : WorkoutExerciseDaoService) : ExerciseCacheDataSource{
        return ExerciseCacheDataSourceImpl(exerciseDaoService, workoutExerciseDaoService)
    }

    @Singleton
    @Provides
    fun provideExerciseSetCacheDataSource(exerciseSetDaoService : ExerciseSetDaoService) : ExerciseSetCacheDataSource{
        return ExerciseSetCacheDataSourceImpl(exerciseSetDaoService)
    }

    @Singleton
    @Provides
    fun provideHistoryWorkoutCacheDataSource(historyWorkoutDaoService : HistoryWorkoutDaoService) : HistoryWorkoutCacheDataSource{
        return HistoryWorkoutCacheDataSourceImpl(historyWorkoutDaoService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseCacheDataSource(historyExerciseDaoService : HistoryExerciseDaoService) : HistoryExerciseCacheDataSource{
        return HistoryExerciseCacheDataSourceImpl(historyExerciseDaoService)
    }

    @Singleton
    @Provides
    fun provideHistoryExerciseSetCacheDataSource(historyExerciseSetDaoService : HistoryExerciseSetDaoService) : HistoryExerciseSetCacheDataSource{
        return HistoryExerciseSetCacheDataSourceImpl(historyExerciseSetDaoService)
    }
}